package com.pjb.springbootjwt.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pjb.springbootjwt.common.exception.ApplicationException;
import com.pjb.springbootjwt.ump.domain.UserDO;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author jinbin
 * @date 2018-07-08 21:04
 */
public class JwtUtils {

    private static Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    public static final String CLAIM_USER_ID = "userId";
    public static final long TOKEN_EXPIRE = 7200000;  //2小时
    public static final long REFREASH_TOKEN_EXPIRE = 86400000;//1天

    public static final String CREATE_SECRET = "c123";
    public static final String REFREASH_SECRET = "r123";

    public static String createToken(String userId) {
        logger.info("创建token userId:{}", userId);
        Date expireDate = new Date(System.currentTimeMillis() + TOKEN_EXPIRE);
        Algorithm algorithm = Algorithm.HMAC256(CREATE_SECRET);
        String token = JWT.create().withClaim(CLAIM_USER_ID, userId).withExpiresAt(expireDate).sign(algorithm);
        return token;
    }

    public static String createRefreashToken(String userId) {
        logger.info("创建refreash token userId:{}", userId);
        Date expireDate = new Date(System.currentTimeMillis() + REFREASH_TOKEN_EXPIRE);
        Algorithm algorithm = Algorithm.HMAC256(REFREASH_SECRET);
        String token = JWT.create().withClaim(CLAIM_USER_ID, userId).withExpiresAt(expireDate).sign(algorithm);
        return token;
    }

    public static boolean verifyToken(String token, String userId) throws ApplicationException {
        logger.info("验证token userId:{}", userId);
        Algorithm algorithm = Algorithm.HMAC256(CREATE_SECRET);
        JWTVerifier jwtVerifier = JWT.require(algorithm).withClaim(CLAIM_USER_ID, userId).build();
        try {
            jwtVerifier.verify(token);
            return true;
        }catch (SignatureVerificationException e){
            logger.info("签名错误:{}", e.getMessage());
            throw new ApplicationException("401", "签名错误");
        }catch (JWTVerificationException e) {
            logger.info("签名超时:{}", e.getMessage());
            throw new ApplicationException("402", "签名超时");
        }catch (Exception e){
            logger.info("验签其他错误:{}", e.getMessage());
            throw new ApplicationException("403", "验签其他错误");
        }
    }

    public static boolean verifyRefreashToken(String token, String userId) throws ApplicationException{
        logger.info("验证refreashToken userId:{}", userId);
        Algorithm algorithm = Algorithm.HMAC256(REFREASH_SECRET);
        JWTVerifier jwtVerifier = JWT.require(algorithm).withClaim(CLAIM_USER_ID, userId).build();
        try {
            jwtVerifier.verify(token);
            return true;
        }catch (SignatureVerificationException e){
            logger.info("签名错误:{}", e.getMessage());
            throw new ApplicationException("501", "签名错误");
        }catch (JWTVerificationException e) {
            logger.info("签名超时:{}", e.getMessage());
            throw new ApplicationException("502", "签名超时");
        }catch (Exception e){
            logger.info("验签其他错误:{}", e.getMessage());
            throw new ApplicationException("503", "验签其他错误");
        }
    }

    /**
     * 获取当前登录用户id
     *
     * @return
     */
    public static String getLoginUserId() {
        Object object = SecurityUtils.getSubject().getPrincipal();
        if (null != object){
            UserDO userDO = (UserDO)object;
            return userDO.getId();
        }
        return null;
    }

    public static String getUserId(String token){
        if (StringUtils.isEmpty(token)){
            return null;
        }

        String userId = null;
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            userId = decodedJWT.getClaim(CLAIM_USER_ID).asString();
        }catch (Exception e){
            logger.info("解密token错误:{}", e.getMessage());
        }

        return userId;
    }
}