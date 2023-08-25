package org.backend.cloud.authentication.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SignatureException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.backend.cloud.authentication.exception.JwtExpiredException;
import org.backend.cloud.authentication.exception.JwtVerifyFailException;
import org.backend.cloud.authentication.model.entity.AuthorizeResult;
import org.backend.cloud.authentication.service.JwtService;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.web.exception.Exceptions;
import org.backend.cloud.web.utils.FileTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService, InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(JwtServiceImpl.class);

  private PrivateKey privateKey;

  private JwtParser jwtParser;

  @Override
  public String createJwt(Object jwtPayload, long expiredAt) {
    //添加构成JWT的参数
    Map<String, Object> headMap = new HashMap();
    headMap.put("alg", SignatureAlgorithm.RS256.getValue());//使用RS256签名算法
    headMap.put("typ", "JWT");
    Map body = JSON.parse(JSON.stringify(jwtPayload), HashMap.class);
    String jwt = Jwts.builder()
        .setHeader(headMap)
        .setClaims(body)
        .setExpiration(new Date(expiredAt))
        .signWith(privateKey)
        .compact();
    return jwt;
  }

  @Override
  public <T> T getPayload(String jwt, Class<T> jwtPayloadClass) {
    if (jwt == null || jwt.equals("")) {
      return null;
    }

    try {
      // jwt字符串由3部分组成，用英文的点分割：herder.payload.sign
      // 可以直接取中间一段，进行Base64解码
      byte[] decodedBytes = Base64.getDecoder().decode(jwt.split("\\.")[1]);
      return JSON.parse(new String(decodedBytes), jwtPayloadClass);
    } catch (Exception e) {
      return null;
    }
  }

  @Override
  public <T> T verifyJwt(String jwt, Class<T> jwtPayloadClass) {
    if (jwt == null || jwt.equals("")) {
      return null;
    }
    // 1. 解析jwt，获取用户数据
    Jws<Claims> jws = null; // 会校验签名，校验过期时间
    try {
      jws = getJwtParser().parseClaimsJws(jwt);
    } catch (ExpiredJwtException e) {
      // Jwt过期，转换异常
      throw new JwtExpiredException();
    } catch (SignatureException e) {
      // Jwt无效，转换异常
      throw new JwtVerifyFailException();
    }

    Claims jwtPayload = jws.getBody();
    if (jwtPayload == null) {
      return null;
    }

//    CurrentUser currentUserInfo = new CurrentUser();
//    currentUserInfo.setUserId(Long.parseLong(jwsBody.get("userId").toString()));
//    currentUserInfo.setRoleId(jwsBody.get("roleId").toString());
//    currentUserInfo.setNickname(jwsBody.get("nickname").toString());
//    currentUserInfo.setSessionId(Long.parseLong(jwsBody.get("sessionId").toString()));
//    currentUserInfo.setExpiredTime(Long.parseLong(jwsBody.get("expiredTime").toString()));
    return JSON.convertTo(jwtPayload, jwtPayloadClass);
  }

  //获取私钥，用于生成Jwt
  public static PrivateKey getPrivateKey() {
    try {
      String filePath = "jwt/jwt_rsa_private.key";
      // 读取Jar包同级目录下 jwt/{filePath} 文件。（生成环境使用）
      String privateKeyBase64 = FileTool.readStringFromTheSameFolderAsJarFile(filePath);
      if (privateKeyBase64 == null) {
        // 读取Jar包内部 src/resource/jwt/{filePath} 文件（开发时使用）
        privateKeyBase64 = FileTool.readStringFromResourceFolder(filePath);
      }
      // 利用JDK自带的工具生成私钥
      KeyFactory kf = KeyFactory.getInstance("RSA");
      PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(Decoders.BASE64.decode(privateKeyBase64));
      return kf.generatePrivate(ks);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      Exceptions.error("获取Jwt私钥失败");
      return null;
    }
  }

  // 公钥，用于解析Jwt
  private JwtParser getJwtParser() {
    try {
      String filePath = "jwt/jwt_rsa_public.key";
      // 读取Jar包同级目录下 jwt/{filePath} 文件。（生成环境使用）
      String publicKeyBase64 = FileTool.readStringFromTheSameFolderAsJarFile(filePath);
      if (publicKeyBase64 == null) {
        // 读取Jar包内部 src/resource/jwt/{filePath} 文件（开发时使用）
        publicKeyBase64 = FileTool.readStringFromResourceFolder(filePath);
      }
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Decoders.BASE64.decode(publicKeyBase64));
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PublicKey pk = keyFactory.generatePublic(keySpec);
      return Jwts.parserBuilder().setSigningKey(pk).build();
    } catch (Exception e) {
      // 获取公钥失败
      throw new RuntimeException(e);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    privateKey = getPrivateKey();
    jwtParser = getJwtParser();
  }
}
