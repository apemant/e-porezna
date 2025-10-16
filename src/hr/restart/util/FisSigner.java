package hr.restart.util;

import hr.apis_it.fin._2012.types.f73.PoslovniProstorZahtjev;
import hr.apis_it.fin._2012.types.f73.RacunZahtjev;
import hr.fina.eracun.b2g.pki.echo.v0.EchoMsg;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.keyinfo.X509IssuerSerial;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Klasa Signer za dodavanje potpisa na xml dokument. Sadrzi metodu za
 * potpisivanje i slanje SOAP poruke putem SSL-a.
 * 
 * Koriste se klase iz JSR 105: XML Digital Signature API-a
 * Koriste se klase iz SAAJ API-a za slanje SOAP poruka putem SSL-a
 * Kirste se klase iz w3c DOM API-a
 *
 * @author Igor Cuncic igor.cuncic@gmail.com 
 * adaptirao andrej@rest-art.hr
 * odabrao Đelo H.
 * tekst čitao ...
 */
public class FisSigner {

    private FisUtil fisUtil;
    
    public FisSigner(FisUtil fu) {
      fisUtil = fu;
    }
    /**
     * Converts RacunZahtjev to w3c document and passes to signDocument  
     * @param zahtjev RacunZahtjev
     * @return signed w3c document
     */
    public Document signJAXBDocument(RacunZahtjev zahtjev) {
      try {
        StringWriter sw = new StringWriter();
        fisUtil.getMarshaller().marshal(zahtjev, sw);
        //debug
//        System.out.println("**** SIGNING: ");
//        System.out.println(sw);
//        System.out.println("******** END");
        return signStr(sw.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }
    /**
     * Converts RacunZahtjev to w3c document and passes to signDocument  
     * @param zahtjev PoslovniProstorZahtjev
     * @return signed w3c document
     */
    public Document signJAXBDocument(PoslovniProstorZahtjev zahtjev) {
      try {
        StringWriter sw = new StringWriter();
        fisUtil.getMarshaller().marshal(zahtjev, sw);
        //debug
//        System.out.println("**** SIGNING: ");
//        System.out.println(sw);
//        System.out.println("******** END");
        return signStr(sw.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }
    
    public Document signJAXBDocument(EchoMsg echo) {
      try {
        StringWriter sw = new StringWriter();
        fisUtil.getMarshaller().marshal(echo, sw);
        //debug
//        System.out.println("**** SIGNING: ");
//        System.out.println(sw);
//        System.out.println("******** END");
        return signStr(sw.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
    }
    
    private Document signStr(String s) throws Exception {
      //konverzija ala idelac zlu netrebalo
      String xmlString = normalize(s);
//      System.out.println("**** NORMALIZED: ");
//      System.out.println(xmlString);
//      System.out.println("******** END");
      InputStream is = new ByteArrayInputStream(xmlString.getBytes());
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      Document ret = dbf.newDocumentBuilder().parse(is);

      fixId(ret.getFirstChild());
      
//      System.out.println("**** DOCUMENT: ");
//      printDOM(ret, System.out);
//      System.out.println("******** END");
      return signDocument(ret);
    }
    
    private Document getDoc(String s) throws Exception {
      String xmlString = normalize(s);
//    System.out.println("**** NORMALIZED: ");
//    System.out.println(xmlString);
//    System.out.println("******** END");
      InputStream is = new ByteArrayInputStream(xmlString.getBytes());
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      dbf.setNamespaceAware(true);
      return dbf.newDocumentBuilder().parse(is);
    }
    
    private void fixId(Node n) {
      if (n == null) return;
      if (n.getNodeType() == n.ELEMENT_NODE)
        fixId((Element) n);
      fixId(n.getFirstChild());
      fixId(n.getNextSibling());
    }
    private void fixId(Element e) {
      String at = e.getAttribute("Id");
      if (at != null && at.length() > 0)
        e.setIdAttribute("Id", true);
    }
    
  //konverzija ala idelac zlu netrebalo
    public String normalize(String sxml) {
      StringBuffer line = new StringBuffer();
      char[] cxml = sxml.toCharArray();
      String xmlString = "";
      for (int i = 0; i < cxml.length; i++) {
        if (cxml[i] == '\n' || cxml[i] == '\r') {
          if (line.length() > 0) {
            xmlString = xmlString+line.toString().trim();
            line = new StringBuffer();
          }
          continue;
        }
        line.append(cxml[i]);
      }
      return xmlString;
    }
    /**
     * Metoda za potpisivanje dokumenata. Koristi Java XMLSign API odnosno
     * JSR105 api za digitalni potpis xml dokumenata.
     * @param doc 
     */
    public Document signDocument(Document doc) {

        try {
            // Stvara DOM XMLSignatureFactory koji ce se koristiti za generiranje
            // potpisa
            //XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM", new org.jcp.xml.dsig.internal.dom.XMLDSigRI()); //1.5
            XMLSignatureFactory factory = XMLSignatureFactory.getInstance("DOM"); //1.6+

            // SHA1 Vrsta digest alogritma za potpisivanje dokumenta
            DigestMethod digestMethod = factory.newDigestMethod(DigestMethod.SHA256, null);

            /* Transformacija - oznacava vrstu transformacije digitalnog potpisa. U ovom 
             * slucaju generira se:
             * algoritam transformacije za enveloped potpis 
             http://en.wikipedia.org/wiki/XML_Signature 
             * alogirtam kanokalizacije na exclusive xml without comments
             http://www.w3.org/TR/xml-exc-c14n/ */
            List<Transform> transformList = new ArrayList<Transform>();
            TransformParameterSpec tps = null;
            Transform envelopedTransform;
            try {
                envelopedTransform = factory.newTransform(Transform.ENVELOPED,
                        tps);
                Transform c14NTransform = factory.newTransform(
                        "http://www.w3.org/2001/10/xml-exc-c14n#", tps);

                transformList.add(envelopedTransform);
                transformList.add(c14NTransform);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("Erro inesperado: " + e.getMessage(), e);
            } catch (InvalidAlgorithmParameterException e) {
                throw new RuntimeException("Erro inesperado: " + e.getMessage(), e);
            }

            /* Reference određuje na koji element unutar xml se generira DigestValue, dodjeljuje mu se preko prametara
             algoritam digesta i transformacija
             */
//            Reference reference = factory.newReference("#RacunZahtjeva", digestMethod, transformList, null, null);
//            Reference reference = factory.newReference("#RacunZahtjev", digestMethod, transformList, null, null);
            Reference reference = factory.newReference("#signXmlId", digestMethod, transformList, null, null);

            /* Popunjava element CanonnicalizationMathod s definiranim algoritmom */
            CanonicalizationMethod canonicalizationMethod = factory.newCanonicalizationMethod(CanonicalizationMethod.EXCLUSIVE, (C14NMethodParameterSpec) null);

            /* Popunjava element SignatureMethod algoritmom za digest 
             * Primjer:
             <SignedInfo>
             <CanonicalizationMethod Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
             <SignatureMethod Algorithm="http://www.w3.org/2000/09/xmldsig#rsa-sha1"/>
             <Reference URI="#RacunZahtjev">
             <Transforms>
             <Transform Algorithm="http://www.w3.org/2000/09/xmldsig#enveloped-signature"/>
             <Transform Algorithm="http://www.w3.org/2001/10/xml-exc-c14n#"/>
             </Transforms>
             <DigestMethod Algorithm="http://www.w3.org/2000/09/xmldsig#sha1"/>
             <DigestValue>VItfxY/A1BITZ/BuWpsGd9gKix4=</DigestValue>
             </Reference>
             </SignedInfo>*/
            SignatureMethod signatureMethod = factory.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256", null);
            SignedInfo signedInfo = factory.newSignedInfo(canonicalizationMethod, signatureMethod, Collections.singletonList(reference));

            // Ucitava kljuceve i certifikat iz keystora
//            KeyStore ks = KeyStore.getInstance("JKS");
//            ks.load(new FileInputStream("democert.jks"), "password".toCharArray());

            KeyStore ks = fisUtil.getKeyStore();
            Enumeration als = ks.aliases();
            String alias = (String) als.nextElement();
            while (alias.equalsIgnoreCase(FisUtil.getMyKey()) || alias.equalsIgnoreCase("fiskalcis")) 
              alias = (String) als.nextElement();

            // Dohvaca privatni kljuc za potpisivanje xml-a
            KeyStore.PrivateKeyEntry keyEntry =
                    (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(fisUtil.getKeyStorePasword().toCharArray()));
            // Dohvaca informacije o certifikatu koji se nalazi u kljucu
            X509Certificate cert = (X509Certificate) keyEntry.getCertificate();

            // KeyInfoFactory dohvaca informacije o kljucu kao sto su issuer, cer. serial itd
            KeyInfoFactory keyInfoFactory = factory.getKeyInfoFactory();

            X509IssuerSerial ser = keyInfoFactory.newX509IssuerSerial(cert.getIssuerX500Principal().getName(), cert.getSerialNumber());
            /* Stvara se lista u kojoj ce se nalazit info o certifikatu i serijskom broju
             * Primjer:
             * <X509IssuerSerial>
             *      <X509IssuerName>OU=DEMO,O=FINA,C=HR</X509IssuerName>
             *      <X509SerialNumber>1234567890</X509SerialNumber>
             * </X509IssuerSerial>
             */
            List x509 = new ArrayList();
            x509.add(cert);
            x509.add(ser);
            X509Data x509Data = keyInfoFactory.newX509Data(x509);

            /* Listom se dodaje unutar elementa KeyInfo element X509Data s popunjenim podatcima 
             * Primjer:
             <KeyInfo>
                <X509Data>
                    <X509Certificate>MIIEyDCCA7CgAwIBAgIEPssQ2TANBgkqh...</X509Certificate>
                    <X509IssuerSerial>
                        <X509IssuerName>OU=DEMO,O=FINA,C=HR</X509IssuerName>
                        <X509SerialNumber>1053495513</X509SerialNumber>
                    </X509IssuerSerial>
                </X509Data>
             </KeyInfo>                     */
            List items = new ArrayList();
            items.add(x509Data);
            KeyInfo ki = keyInfoFactory.newKeyInfo(items);
// ai: ovo je sve manje potrebno jer dokument dobivamo iz JAXBElementa na foru vuk-lisica
//            /*
//             * Stvara se dokument factory u kojeg ce se ucitati XML
//             */
//            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//            dbf.setNamespaceAware(true);
//            /*
//             * Dio koda preuzet sa stranice http://fiskalizacija.codeplex.com/discussions/404204
//             * od korisnika idelac.
//             * Kod ucitava XML u String i uklanja prazan prostor "whitespace" unutar xml dokumenta
//             */
//            String thisLine = "";
//            String xmlString = "";
//            BufferedReader br = new BufferedReader(new FileReader("c:\\file.xml"));
//            br.readLine();
//            while ((thisLine = br.readLine()) != null) {
//                xmlString = xmlString + thisLine.trim();
//            }
//            br.close();
//            /*
//             * Kreira se ByteArrayInputStream od Stringa koji predstavlja xml podatke
//             */
//            ByteArrayInputStream xmlStream = new ByteArrayInputStream(xmlString.getBytes());
//            /*
//             * Stvaranj xml Dokumenta iz stream-a
//             */
//            Document doc = dbf.newDocumentBuilder().parse(xmlStream);

            /*
             * Priprema dokumenta za digitalan potpis
             */
            DOMSignContext dsc = new DOMSignContext(keyEntry.getPrivateKey(), doc.getDocumentElement());
            /*
             * Stvaranje digitalnog potpisa pomocu kljuca i prethodno definiranih podataka 
             * (kanonikalizacija, algoritmi, digest, referenca itd.)
             */
            XMLSignature signature = factory.newXMLSignature(signedInfo, ki);

            /*
             * Potpisivanje xml dokumenta
             */
            signature.sign(dsc);
            /*
             * Slanje potpisanog dokumenta metodom sendSOAP(doc) 
             */
           // sendSOAP(doc);
            return doc;

        } catch (Exception ex) {
          ex.printStackTrace();
        }
        return null;
    }
    /**
     * Metoda koja salje SOAP poruku CIS WS-u putem SSL-a
     * 
     * @param doc Potpisani XML dokument kojeg saljem SOAP-SSLom
     */
    public SOAPMessage sendSOAP(Document doc) {
        try {
            /*
             * Dio koda preuzet s http://blog.hexican.com/2010/12/sending-soap-messages-through-https-using-saaj/
             */
//            System.setProperty("javax.net.ssl.keyStore", "democert.jks");
//            System.setProperty("javax.net.ssl.keyStorePassword", "password");
//            System.setProperty("javax.net.ssl.keyStoreType", "JKS");
          
          
            //System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            //Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

            //First create the connection
            SOAPConnectionFactory soapConnFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = soapConnFactory.createConnection();

            //Next, create the actual message
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage message = messageFactory.createMessage();

            // Create objects for the message parts
            SOAPPart soapPart = message.getSOAPPart();
            SOAPEnvelope envelope = soapPart.getEnvelope();
            SOAPBody body = envelope.getBody();
//            envelope = soapPart.getEnvelope();
            // Micanje header-a iz poruke
            envelope.getHeader().detachNode();
//            body = envelope.getBody();
            
            //Populate the Message
//            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//            dbFactory.setNamespaceAware(true);
            
            // Dodaje potpisani XML u body SOAP-a
//            body.addNamespaceDeclaration("ns2", "http://www.w3.org/2000/09/xmldsig#");
//            body.addNamespaceDeclaration("tns", "http://www.apis-it.hr/fin/2012/types/f73");
            envelope.addNamespaceDeclaration("xsd", "http://www.w3.org/2001/XMLSchema");
            envelope.addNamespaceDeclaration("xsi", "http://www.w3.org/2001/XMLSchema-instance");
//            ((CoreDocumentImpl)body.getOwnerDocument()).setErrorChecking(false);
            body.addDocument(doc);
//            System.err.println(
//                body.getFirstChild().getNodeName()
//                );
//            ((SOAPElement)body.getFirstChild()).addNamespaceDeclaration("tns", "http://www.apis-it.hr/fin/2012/types/f73");
            message.saveChanges();

            // Check the input
            System.out.println("\nREQUEST:\n");
//            message.writeTo(System.out);
            message.writeTo(new FileOutputStream("soap.xml"));
            System.out.println();

            //Set the destination
//            URL destination = new URL("https://cistest.apis-it.hr:8449/FiskalizacijaServiceTest");
            URL destination = new URL(fisUtil.getSrvURL());
            
            // Send the message
            SOAPMessage reply = connection.call(message, destination);
            
            return reply;

            /*
             * Grešku o slanju poruke generira u error.txt datoteku
             */
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void transform(SOAPMessage reply) throws Exception { // iliti resto od sendSoap
      //Extract the content of the reply
      Source sourceContent = reply.getSOAPPart().getContent();

//      //Set the output for the transformation
//      FileOutputStream out; // declare a file output object
//      PrintStream p; // declare a print stream object
//      
//      // Create a new file output stream
//      // Odgovor servisa sprema u response.xml datoteku
//      out = new FileOutputStream("c:\\response.xml");
//      
//      // Connect print stream to the output stream
//      p = new PrintStream(out);
//      StreamResult result = new StreamResult(p);
      
      StreamResult result = new StreamResult(System.out);
      //Create the transformer
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.transform(sourceContent, result);
//      p.println();
//      p.close();
      
      //Close the connection
//      connection.close();
    }
    public void printDOM(Document doc, PrintStream out) {
      printDOM(new DOMSource(doc), out);
    }
    public void printDOM(Source src, PrintStream out) {
      printDOM(src, new StreamResult(out));
    }
    public void printDOM(Source src, StreamResult srout) {
      Transformer transformer;
      try {
        transformer = TransformerFactory.newInstance().newTransformer();
        //
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        //
        transformer.transform(src, srout);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}
