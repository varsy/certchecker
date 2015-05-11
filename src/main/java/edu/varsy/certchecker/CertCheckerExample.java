package edu.varsy.certchecker;


import edu.varsy.certchecker.business.CertChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Project: certchecker
 * User: vars
 * Date: 04/05/15
 * Time: 21:24
 * Created with IntelliJ IDEA.
 */
public class CertCheckerExample {

    private static Logger log = LoggerFactory.getLogger(CertCheckerExample.class);

    public static void main(String[] args) {

        CertChecker certChecker = new CertChecker();

        certChecker.checkCertificate("https://github.com");
        certChecker.checkCertificate("http://legion-stroy.com");
        certChecker.checkCertificate("https://test.upsource.aws.intellij.net");
        certChecker.checkCertificate("https://storage.calisto.email");

    }
}
