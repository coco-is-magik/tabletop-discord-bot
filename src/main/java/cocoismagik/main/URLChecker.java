package cocoismagik.main;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URLChecker {

    private static final String COMMAND_INJECTION_REGEX = "[;&|<>`$\\\\/]|\\b(DANGER_KEYWORD)\\b|\\$\\(|\\)";
    private static final List<String> DANGER_KEYWORDS = new ArrayList<>();
    private static final Map<String, String> ALLOWED_PARAMS = new HashMap<>();
    static {
        ALLOWED_PARAMS.put("format", "^(jpeg|jpg|png|gif|webp)$");
        ALLOWED_PARAMS.put("width", "^\\d{1,5}$");
        ALLOWED_PARAMS.put("height", "^\\d{1,5}$");
        ALLOWED_PARAMS.put("ex", "^[a-zA-Z0-9]{1,16}$");
        ALLOWED_PARAMS.put("is", "^[a-zA-Z0-9]{1,16}$");
        ALLOWED_PARAMS.put("hm", "^[a-zA-Z0-9]{1,64}$");
        ALLOWED_PARAMS.put("f", "^[01]$");
        ALLOWED_PARAMS.put("nofb", "^[01]$");
        ALLOWED_PARAMS.put("ipt", "^[a-fA-F0-9]{64}$");
        ALLOWED_PARAMS.put("ipo", "^[a-zA-Z]+$");
        ALLOWED_PARAMS.put("cs", "^srgb$");
        ALLOWED_PARAMS.put("dl", "^[a-zA-Z0-9\\-\\.]+$");
        ALLOWED_PARAMS.put("fm", "^(jpeg|jpg|png|gif|webp)$");
        ALLOWED_PARAMS.put("", "^$");
        ALLOWED_PARAMS.put("quality", "^(100|[1-9]?[0-9]|[a-zA-Z]{1,10})$");
        DANGER_KEYWORDS.add("sh");
        DANGER_KEYWORDS.add("bash");
        DANGER_KEYWORDS.add("env");
        DANGER_KEYWORDS.add("exec");
        DANGER_KEYWORDS.add("system");
        DANGER_KEYWORDS.add("cat");
        DANGER_KEYWORDS.add("wget");
        DANGER_KEYWORDS.add("curl");
        DANGER_KEYWORDS.add("python");
        DANGER_KEYWORDS.add("perl");
        DANGER_KEYWORDS.add("php");
        DANGER_KEYWORDS.add("ruby");
        DANGER_KEYWORDS.add("rm");
        DANGER_KEYWORDS.add("mv");
        DANGER_KEYWORDS.add("cp");
        DANGER_KEYWORDS.add("ln");
        DANGER_KEYWORDS.add("tar");
        DANGER_KEYWORDS.add("gzip");
        DANGER_KEYWORDS.add("bzip2");
        DANGER_KEYWORDS.add("zip");
        DANGER_KEYWORDS.add("unzip");
        DANGER_KEYWORDS.add("sudo");
        DANGER_KEYWORDS.add("kill");
        DANGER_KEYWORDS.add("ps");
        DANGER_KEYWORDS.add("netstat");
        DANGER_KEYWORDS.add("ifconfig");
        DANGER_KEYWORDS.add("mkfs");
        DANGER_KEYWORDS.add("mount");
        DANGER_KEYWORDS.add("umount");
        DANGER_KEYWORDS.add("ssh");
        DANGER_KEYWORDS.add("scp");
    }

    private static String decodeUrl(String encodedUrl) {
        try {
            return URLDecoder.decode(encodedUrl, "UTF-8");
        } catch (Exception e) {
            DataOutputter.logMessage("Failed to decode URL: " + encodedUrl + ", error: " + e.getMessage(), DataOutputter.ERROR);
            throw new IllegalArgumentException("Error decoding URL: " + e.getMessage());
        }
    }

    private static Map<String, String> getQueryParameters(String urlString) {
        Map<String, String> queryParameters = new HashMap<>();
        try {
            URL url = new URL(urlString);
            String query = url.getQuery(); // Extract the query string
            if (query == null) return queryParameters;

            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=", 2);
                String key = URLDecoder.decode(keyValue[0], "UTF-8");
                String value = keyValue.length > 1 ? URLDecoder.decode(keyValue[1], "UTF-8") : "";
                queryParameters.put(key, value);
            }
        } catch (Exception e) {
            DataOutputter.logMessage("Failed to parse query parameters: " + e.getMessage(), DataOutputter.ERROR);
        }
        return queryParameters;
    }

    public static boolean isSafeUrl(String url, String[] validFileTypes) {
        //TODO: query a list of known malicious sites to auto-reject those domains

        if (url == null || url.isEmpty()) {
            DataOutputter.logMessage("Empty or null URL provided", DataOutputter.WARNING);
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        if (url.length() > 2000) {
            DataOutputter.logMessage("Detected excessive URL length", DataOutputter.WARNING);
            throw new IllegalArgumentException("URL is too long");
        }

        //FIXME: triggers on everything, dont fucking know why
        /* 
        // look for double encoding in the url, or incorrect incoding
        String reEncodedUrl;
        try {
            reEncodedUrl = URLEncoder.encode(URLDecoder.decode(url, "UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            DataOutputter.logMessage("Failed to validate encoding for URL: " + url + ", error: " + e.getMessage(), DataOutputter.ERROR);
            throw new IllegalArgumentException("Error decoding URL: " + e.getMessage());
        }
        if (!url.equals(reEncodedUrl)) {
            DataOutputter.logMessage("URL encoding mismatch detected", DataOutputter.WARNING);
            throw new IllegalArgumentException("URL encoding mismatch detected");
        }*/

        // Check basic url formatting to ensure certificates and things are used
        if (!url.startsWith("https://")) {
            DataOutputter.logMessage("Detected URL without https", DataOutputter.WARNING);
            throw new IllegalArgumentException("URL does not have https");
        }

        // Check if the URL is a valid image to download
        if (validFileTypes != null) {
            boolean matchedFileType = false;
            for (String fileType : validFileTypes) {
                // We are wary of things whose file type cannot be verified as valid
                if (url.matches("(?i)\\S{1,}\\." + fileType + "\\S{0,}")) {
                    matchedFileType = true;
                    break;
                }
            }
            if (!matchedFileType) {
                DataOutputter.logMessage("Detected URL without valid file extension", DataOutputter.WARNING);
                throw new IllegalArgumentException("URL does not have valid file extension");
            }
        }

        // Validate parameters in the URL to ensure no funny business
        Map<String, String> queryParameters = getQueryParameters(url);
        queryParameters.forEach((key, value) -> {
            // Special case for embedded urls:
            if ("u".equals(key)) {
                DataOutputter.logMessage("Found embedded URL from recognized parameter 'u'", DataOutputter.INFO);
                String decodedUrl = decodeUrl(value);
                try {
                    isSafeUrl(decodedUrl, validFileTypes);
                } catch (IllegalArgumentException e) {
                    // throw it back up to the caller
                    throw e;
                }
                return;
            }

            // Hard limit for parameter length, will supercede any regex limit if it is higher
            if (key.length() > 256 || value.length() > 256) {
                DataOutputter.logMessage("Query parameter exceeds length limit: " + key + "=" + value, DataOutputter.WARNING);
                throw new IllegalArgumentException("Query parameter exceeds length limit: " + key);
            }

            if (!ALLOWED_PARAMS.containsKey(key)) {
                DataOutputter.logMessage("Detected URL with disallowed parameter", DataOutputter.WARNING);
                throw new IllegalArgumentException("URL contains disallowed parameter: " + key);
            }

            if (!value.matches(ALLOWED_PARAMS.get(key))) {
                DataOutputter.logMessage("Detected URL with disallowed parameter value", DataOutputter.WARNING);
                throw new IllegalArgumentException("URL contains disallowed parameter value for " + key + ": " + value);
            }

            for (String dangerKeyword : DANGER_KEYWORDS) {
                String dangerRegex = COMMAND_INJECTION_REGEX.replace("DANGER_KEYWORD", dangerKeyword);
                if (key.matches(dangerRegex)) {
                    DataOutputter.logMessage("Detected URL with potential command injection in key"+key, DataOutputter.WARNING);
                    throw new IllegalArgumentException("URL contains something that looks like it has command injection: " + key);
                }
                if (value.matches(dangerRegex)) {
                    DataOutputter.logMessage("Detected URL with potential command injection in value: "+value, DataOutputter.WARNING);
                    throw new IllegalArgumentException("URL contains something that looks like it has command injection: " + value);
                }
            }
        });

        return true;
    }
    
}
