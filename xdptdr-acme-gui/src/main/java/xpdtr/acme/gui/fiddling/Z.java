package xpdtr.acme.gui.fiddling;

import java.util.Base64;

import xdptdr.common.Common;

public class Z {
	public static void main(String[] args) {

		String s1 = "MIIGFzCCBP+gAwIBAgISA8T74rnswmCPLgwzKnKRT7NkMA0GCSqGSIb3DQEBCwUA"
				+ "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD"
				+ "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0xODA3MDUxODI5NDVaFw0x"
				+ "ODEwMDMxODI5NDVaMCIxIDAeBgNVBAMTF2Nob3VwaXByb2plY3QuaG9wdG8ub3Jn"
				+ "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAra9g7oo3hWvvcWUP31F6"
				+ "X6ng67Hvn/DOXzqz9A9mwcRgyYLWEYSCUUhj9I71qMHAzNVGx+GHHbuu51qSp8pk"
				+ "RBwDWHhoaVMKlco54BcRx7q8zJzqHnq3p9hEdhWmdS3yLzV6jGYckKZwWpD2oFRk"
				+ "MdhudjVxQU9nqJvS6tvRc7deRbuKcDi3OnBdnbmCvHwwwzF0kqCezRspAnawYZnG"
				+ "xQzG/v75Fwm04IAbnhuhJwUEyuAsPBGI6K7TGdHNRN4n6Y0JAdq2DgKHdnt84YkY"
				+ "2U9fN9lmhveP9MFLrrFCT2fyU8yHnKRsXVxNPjNKoUws1eLzCfHKglGHBhK9bXAq"
				+ "YwIDAQABo4IDHTCCAxkwDgYDVR0PAQH/BAQDAgWgMB0GA1UdJQQWMBQGCCsGAQUF"
				+ "BwMBBggrBgEFBQcDAjAMBgNVHRMBAf8EAjAAMB0GA1UdDgQWBBSp9dTdVpgDse0J"
				+ "hNtY9HHsBkvIZDAfBgNVHSMEGDAWgBSoSmpjBH3duubRObemRWXv86jsoTBvBggr"
				+ "BgEFBQcBAQRjMGEwLgYIKwYBBQUHMAGGImh0dHA6Ly9vY3NwLmludC14My5sZXRz"
				+ "ZW5jcnlwdC5vcmcwLwYIKwYBBQUHMAKGI2h0dHA6Ly9jZXJ0LmludC14My5sZXRz"
				+ "ZW5jcnlwdC5vcmcvMCIGA1UdEQQbMBmCF2Nob3VwaXByb2plY3QuaG9wdG8ub3Jn"
				+ "MIH+BgNVHSAEgfYwgfMwCAYGZ4EMAQIBMIHmBgsrBgEEAYLfEwEBATCB1jAmBggr"
				+ "BgEFBQcCARYaaHR0cDovL2Nwcy5sZXRzZW5jcnlwdC5vcmcwgasGCCsGAQUFBwIC"
				+ "MIGeDIGbVGhpcyBDZXJ0aWZpY2F0ZSBtYXkgb25seSBiZSByZWxpZWQgdXBvbiBi"
				+ "eSBSZWx5aW5nIFBhcnRpZXMgYW5kIG9ubHkgaW4gYWNjb3JkYW5jZSB3aXRoIHRo"
				+ "ZSBDZXJ0aWZpY2F0ZSBQb2xpY3kgZm91bmQgYXQgaHR0cHM6Ly9sZXRzZW5jcnlw"
				+ "dC5vcmcvcmVwb3NpdG9yeS8wggECBgorBgEEAdZ5AgQCBIHzBIHwAO4AdQDbdK/u"
				+ "yynssf7KPnFtLOW5qrs294Rxg8ddnU83th+/ZAAAAWRr6/4MAAAEAwBGMEQCIFmh"
				+ "x82gzM/93CHmiu/ugczEw/01drd+zSBQJqb/DWgeAiBMz0nC/brDzgnftwE87pUV"
				+ "Bx/DBC65gYOISubyluz1+QB1ACk8UZZUyDlluqpQ/FgH1Ldvv1h6KXLcpMMM9OVF"
				+ "R/R4AAABZGvr/h0AAAQDAEYwRAIgK4Iv7V61i3KiCHqVtkMB4f/D2G6KyB6UJqmT"
				+ "jOnyqRgCIGpnNmZyg6qHDidC9tcxoL6oKWxZVWHUZ/eY3Rp3JjmTMA0GCSqGSIb3"
				+ "DQEBCwUAA4IBAQBW7j80Xhf5rt5hxE8xkENCCqeOUrWItariUWbDvfOywuvBMOJL"
				+ "6Wt5YYdnbVYdcObNDYAJcPsxZuffHO8LtImHqc5dJD5J8kZAKmiaK+p9bO0CeEg+"
				+ "BU14q8DgY+8EKiT1svYU+Mr7CjUrJSddaBQJRDYT6azPj3Lkffx+mii6/9JAs9zE"
				+ "PtrX5kSbW7adYDvEoxoYVY+nrD2g+vDVwJLfqs6AtngJ+r5VUdPjg4siEgnr+SMQ"
				+ "7dllBVF86oeAEMxls9hSuUqiFVbyX1lBabMbIB76mAjsSWaF7RTLcatErezvusk5"
				+ "CvseVtJxBwTfxzLTsdUJbQwuAvrAkoG1HDmz";

		System.out.println(Common.bytesToString(Base64.getDecoder().decode(s1)));

	}
}
