/*
 * Copyright 2020 Google LLC
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

package com.example.cloudrun;

// [START eventarc_generic_handler]

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class EventController {


    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity receiveMessage(
            @RequestBody String body, @RequestHeader Map<String, String> headers) throws JSONException {



        JSONObject jsonBody = new JSONObject(body);

        if(jsonBody.getJSONObject("resource").get("type").equals("gcs_bucket")
            && jsonBody.getJSONObject("resource").getJSONObject("labels").get("bucket_name").equals("pmm-test-bucket")
            && jsonBody.getJSONObject("protoPayload").getString("resourceName").contains("pmm-test-bucket"))
        {

            System.out.println("Event import request received!");

            List<String> requiredFields = Arrays.asList("ce-methodname", "ce-servicename", "ce-resourcename", "ce-subject");

            for (String field : requiredFields) {
                if (headers.get(field) == null) {
                    String msg = String.format("Fail Event import, expected missing header: %s.", field);
                    System.out.println(msg);
                    
                }
            }

            String resourceName = headers.get("ce-resourcename");

            String[] importData = resourceName.split("/");

            int clientId = Integer.parseInt(importData[5]);
            String tableId =importData[6];
            String fileName = importData[7];

            String bucketName = jsonBody.getJSONObject("resource").getJSONObject("labels").getString("bucket_name");
            String projectId = jsonBody.getJSONObject("resource").getJSONObject("labels").getString("project_id");
            String filePath = clientId+"/"+tableId+"/"+fileName;
            //eventarcFileImportService.fileImport(clientId,tableId,bucketName,filePath);
System.out.println("Event import request Done!"+bucketName+"||"+projectId+"||"+fileName+"||"+filePath+"||"+tableId+"||"+clientId+"||"+fileName);
        }
        return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
}
// [END eventarc_generic_handler]
