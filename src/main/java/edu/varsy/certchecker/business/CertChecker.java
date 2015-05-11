package edu.varsy.certchecker.business;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

/**
 * Project: certchecker
 * User: vars
 * Date: 04/05/15
 * Time: 21:24
 * Created with IntelliJ IDEA.
 */
public class CertChecker {

    private static Logger log = LoggerFactory.getLogger(CertChecker.class);

    public void checkCertificate(String checkUrl) {

        URL url = null;
        try {
            url = new URL(checkUrl);
        } catch (MalformedURLException e) {
            log.error("Error: bad url provided - {}", checkUrl, e);
            return;
        }


        URLConnection conn = null;
        try {
            conn = url.openConnection();
        } catch (IOException e) {
            log.error("Error: can't open connection to {}", url, e);
            return;
        }

        if (conn instanceof HttpsURLConnection) {
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;

            try {
                log.debug("Response code for {}: {}", url, httpsConn.getResponseCode());
            } catch (SSLHandshakeException e) {
                log.error("Error: can't validate certificate for {}", url);
                log.debug("Stacktrace: ", e);
                return;
            } catch (SSLProtocolException e) {
                log.error("Error: certificate name mismatch for {}", url);
                log.debug("Stacktrace: ", e);
                return;
            } catch (IOException e) {
                log.error("Error: can't get response code from {}", url, e);
                return;
            }

            X509Certificate[] serverCertificates = new X509Certificate[0];
            try {
                serverCertificates = (X509Certificate[]) httpsConn.getServerCertificates();
            } catch (SSLPeerUnverifiedException e) {
                log.error("Error: can't get server certificate from {}", url, e);
            }
            int i = 0;
            for (X509Certificate cert : serverCertificates) {
                log.info("Printing certificate #{}", i);
                log.info("SubjectDN: {}", cert.getSubjectDN());
                log.info("Not Valid Before: {}", cert.getNotBefore());
                log.info("Not Valid After: {}", cert.getNotAfter());
                try {
                    cert.checkValidity();
                } catch (CertificateExpiredException e) {
                    log.error("Certificate is expired! Expiration date: {}", cert.getNotAfter(), e);
                } catch (CertificateNotYetValidException e) {
                    log.error("Certificate is not yet valid! Starting date: {}", cert.getNotBefore(), e);
                }
                i++;
            }
            httpsConn.disconnect();
        } else {
            log.info("There is no HTTPS server: {}", checkUrl);
        }


    }

}
