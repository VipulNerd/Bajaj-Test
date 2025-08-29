package com.example;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dto.BfhlResponse;
import com.dto.InputRequest;

@RestController
public class BfhlController {

    // --- CHANGE THESE to your details before submitting ---
    private static final String FULL_NAME_LOWER = "john_doe";    // must be lowercase
    private static final String DOB_DDMMYYYY = "17091999";      // ddmmyyyy
    private static final String EMAIL = "john@xyz.com";
    private static final String ROLL_NUMBER = "ABCD123";
    // ----------------------------------------------------

    @PostMapping("/bfhl")
    public ResponseEntity<BfhlResponse> process(@RequestBody InputRequest req) {
        BfhlResponse res = new BfhlResponse();

        try {
            List<String> data = req.getData();
            if (data == null) {
                data = Collections.emptyList();
            }

            List<String> oddNumbers = new ArrayList<>();
            List<String> evenNumbers = new ArrayList<>();
            List<String> alphabets = new ArrayList<>();
            List<String> specialCharacters = new ArrayList<>();
            BigInteger sum = BigInteger.ZERO;

            StringBuilder alphaCharsBuilder = new StringBuilder();

            for (String item : data) {
                if (item == null) {
                    continue;
                }
                // If string is fully numeric (optionally negative) -> number
                if (item.matches("-?\\d+")) {
                    BigInteger val = new BigInteger(item);
                    sum = sum.add(val);
                    BigInteger rem = val.abs().mod(BigInteger.valueOf(2));
                    if (rem.equals(BigInteger.ZERO)) {
                        evenNumbers.add(item); // keep as string
                    } else {
                        oddNumbers.add(item);
                    }
                    // no alphabet chars inside a pure number
                } else {
                    // extract any alphabetic characters for concat_string (preserve original case)
                    for (int i = 0; i < item.length(); i++) {
                        char c = item.charAt(i);
                        if (Character.isLetter(c)) {
                            alphaCharsBuilder.append(c);
                        }
                    }

                    // if the whole item is alphabets -> add to alphabets array (converted to uppercase)
                    if (item.matches("[a-zA-Z]+")) {
                        alphabets.add(item.toUpperCase());
                    } else {
                        // mixed or pure-special -> treat as special characters entry
                        specialCharacters.add(item);
                    }
                }
            }

            // Build concat_string: reverse the collected alphabetic characters, then alternating caps (start UPPER)
            String reversed = alphaCharsBuilder.reverse().toString();
            StringBuilder altCaps = new StringBuilder();
            for (int i = 0; i < reversed.length(); i++) {
                char c = reversed.charAt(i);
                altCaps.append(i % 2 == 0 ? Character.toUpperCase(c) : Character.toLowerCase(c));
            }

            res.setIsSuccess(true);
            res.setUserId(FULL_NAME_LOWER + "_" + DOB_DDMMYYYY);
            res.setEmail(EMAIL);
            res.setRollNumber(ROLL_NUMBER);
            res.setOddNumbers(oddNumbers);
            res.setEvenNumbers(evenNumbers);
            res.setAlphabets(alphabets);
            res.setSpecialCharacters(specialCharacters);
            res.setSum(sum.toString()); // sum as string
            res.setConcatString(altCaps.toString());

            return ResponseEntity.ok(res);

        } catch (Exception ex) {
            // On any error, return is_success false and graceful empty structures
            res.setIsSuccess(false);
            res.setUserId(FULL_NAME_LOWER + "_" + DOB_DDMMYYYY);
            res.setEmail(EMAIL);
            res.setRollNumber(ROLL_NUMBER);
            res.setOddNumbers(Collections.emptyList());
            res.setEvenNumbers(Collections.emptyList());
            res.setAlphabets(Collections.emptyList());
            res.setSpecialCharacters(Collections.emptyList());
            res.setSum("0");
            res.setConcatString("");
            return ResponseEntity.ok(res);
        }
    }
}
