package xdptdr.asn;

import java.util.HashMap;
import java.util.Map;

public class OIDS {

	public static final String RSA = "1.2.840.113549.1.1.1";
	public static final String SHA256RSA = "1.2.840.113549.1.1.11";
	public static final String EXTENSION_REQUEST = "1.2.840.113549.1.9.14";
	public static final String CLIENT_AUTH = "1.3.6.1.5.5.7.3.2";

	public static final String COMMON_NAME = "2.5.4.3";
	public static final String COUNTRY_NAME = "2.5.4.6";
	public static final String LOCALITY_NAME = "2.5.4.7";
	public static final String STATE_OR_PROVINCE_NAME = "2.5.4.8";
	public static final String ORGANIZATION_NAME = "2.5.4.10";
	public static final String ORGANIZATIONAL_UNIT_NAME = "2.5.4.11";
	public static final String SUBJECT_KEY_IDENTIFIER = "2.5.29.14";
	public static final String KEY_USAGE = "2.5.29.15";
	public static final String BASIC_CONSTRAINTS = "2.5.29.19";
	public static final String AUTHORITY_KEY_IDENTIFIER = "2.5.29.35";
	public static final String EXT_KEY_USAGE = "2.5.29.37";
	public static final String HASH_ALGORITHM_IDENTIFIER = "1.3.14.3.2.26";
	public static final String DATA = "1.2.840.113549.1.7.1";
	public static final String ENCRYPTED_DATA = "1.2.840.113549.1.7.6";
	public static final String PBE_WITH_SHA_AND_40_BIT_RC2_CBC = "1.2.840.113549.1.12.1.6";
	public static final String PKCS_8_SHROUDED_KEY_BAG = "1.2.840.113549.1.12.10.1.2";
	public static final String PBE_WITH_SHA_AND_3_KEY_TRIPLE_DES_CBC = "1.2.840.113549.1.12.1.3";
	public static final String FRIENDLY_NAME = "1.2.840.113549.1.9.20";
	public static final String LOCAL_KEY_ID = "1.2.840.113549.1.9.21";
	public static final String CERT_BAG = "1.2.840.113549.1.12.10.1.3";
	public static final String X509_CERTIFICATE = "1.2.840.113549.1.9.22.1";
	public static final String EC_PUBLIC_KEY = "1.2.840.10045.2.1";
	public static final String PRIME_256_V1 = "1.2.840.10045.3.1.7";
	public static final String DSA = "1.2.840.10040.4.1";
	public static final String DH_KEY_AGREEMENT = "1.2.840.113549.1.3.1";
	
	public static final String ID_PE_ACME_IDENTIFIER_V1 = "1.3.6.1.5.5.7.1.30.1"; // as specified in TLS ALPN draft but conflicts in the SMI numbers registry 
	public static final String SUBJECT_ALTERNATIVE_NAME = "2.5.29.17"; // as specified in TLS ALPN draft but conflicts in the SMI numbers registry 

	private static Map<String, String> oidLabels = new HashMap<>();
	static {
		oidLabels.put(SHA256RSA, "SHA256withRSA");
		oidLabels.put(RSA, "RSA");
		oidLabels.put(SUBJECT_KEY_IDENTIFIER, "subjectKeyIdentifier");
		oidLabels.put(COUNTRY_NAME, "countryName");
		oidLabels.put(STATE_OR_PROVINCE_NAME, "stateOrProvinceName");
		oidLabels.put(LOCALITY_NAME, "localityName");
		oidLabels.put(ORGANIZATION_NAME, "organizationName");
		oidLabels.put(ORGANIZATIONAL_UNIT_NAME, "organizationalUnitName");
		oidLabels.put(COMMON_NAME, "commonName");
		oidLabels.put(SUBJECT_KEY_IDENTIFIER, "subjectKeyIdentifier");
		oidLabels.put(KEY_USAGE, "keyUsage");
		oidLabels.put(BASIC_CONSTRAINTS, "basicConstraints");
		oidLabels.put(EXTENSION_REQUEST, "extensionRequest");
		oidLabels.put(AUTHORITY_KEY_IDENTIFIER, "authorityKeyIdentifier");
		oidLabels.put(EXT_KEY_USAGE, "extKeyUsage");
		oidLabels.put(CLIENT_AUTH, "clientAuth");
		oidLabels.put(HASH_ALGORITHM_IDENTIFIER, "hashAlgorithmIdentifier");
		oidLabels.put(DATA, "data");
		oidLabels.put(ENCRYPTED_DATA, "encryptedData");
		oidLabels.put(PBE_WITH_SHA_AND_40_BIT_RC2_CBC, "pbeWithSHAAnd40BitRC2-CBC");
		oidLabels.put(PKCS_8_SHROUDED_KEY_BAG, "pkcs-8ShroudedKeyBag");
		oidLabels.put(PBE_WITH_SHA_AND_3_KEY_TRIPLE_DES_CBC, "pbeWithSHAAnd3-KeyTripleDES-CBC");
		oidLabels.put(FRIENDLY_NAME, "friendlyName");
		oidLabels.put(LOCAL_KEY_ID, "localKeyID");
		oidLabels.put(CERT_BAG, "certBag");
		oidLabels.put(X509_CERTIFICATE, "x509Certificate");
		oidLabels.put(EC_PUBLIC_KEY, "ecPublicKey");
		oidLabels.put(PRIME_256_V1, "prime256v1");
		oidLabels.put(DSA, "DSA");
		oidLabels.put(DH_KEY_AGREEMENT, "dhKeyAgreement");
		oidLabels.put(ID_PE_ACME_IDENTIFIER_V1, "id-pe-acmeIdentifier-v1");
		oidLabels.put(SUBJECT_ALTERNATIVE_NAME, "subjectAlternativeName");
	}

	public static String getLabel(String oid) {
		String label = oidLabels.get(oid);
		if (label == null) {
			System.out.println("No label found for oid " + oid);
			return oid;
		}
		return label;
	}

}
