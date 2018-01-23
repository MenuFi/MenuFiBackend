/**
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.menufi.backend.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.Map;

@RestController
public class HelloworldController {
    Connection conn;
    boolean initialized = false;

    @GetMapping("/")
    public String hello() {
        return response;
    }

    public String sqlTest() {
        if (!initialized) {
            initConnection();
            initialized = true;
        }
        String response = "No Response";
        String query = "SELECT * FROM patron_login;";
        try {
            ResultSet rs = conn.prepareStatement(query).executeQuery();
            rs.next();
            response = rs.getString("email");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

    public void initConnection() {
        try {
            ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
            Map<String,Object> attr = env.getAttributes();
            String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");

            String url = hostname.contains("localhost:")
                    ? System.getProperty("cloudsql-local") : System.getProperty("cloudsql");
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }
}
