/**
 *
 * (c) Copyright Ascensio System SIA 2023
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.flash.onlyoffice.domain.util.service;

import org.json.simple.JSONObject;

import java.io.InputStream;


// specify the converter service functions
public interface ServiceConverter {
    // get the URL to the converted file
    String getConvertedUri(String documentUri, String fromExtension,
                                  String toExtension, String documentRevisionId,
                                  String filePass, Boolean isAsync, String lang);
    String generateRevisionId(String expectedKey);  // generate document key
    String convertStreamToString(InputStream stream);  // convert stream to string
    JSONObject convertStringToJSON(String jsonString);  // convert string to json
}