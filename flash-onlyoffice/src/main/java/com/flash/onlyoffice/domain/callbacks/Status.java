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

package com.flash.onlyoffice.domain.callbacks;

/**
 * @author zhangsp
 */

public enum Status {
    /**
     * 枚举
     */
    // 1 - document is being edited
    EDITING(1),
    // 2 - document is ready for saving
    SAVE(2),
    // 3 - document saving error has occurred
    CORRUPTED(3),
    // 6 - document is being edited, but the current document state is saved
    MUST_FORCE_SAVE(6),
    // 7 - error has occurred while force saving the document
    CORRUPTED_FORCE_SAVE(7);
    private int code;
    Status(final int codeParam) {
        this.code = codeParam;
    }
    public int getCode() {  // get document status
        return this.code;
    }

    public static Status of(int code) {
        if (EDITING.code == code) {
            return EDITING;
        }
        if (SAVE.code == code) {
            return SAVE;
        }
        if (CORRUPTED.code == code) {
            return CORRUPTED;
        }
        if (MUST_FORCE_SAVE.code == code) {
            return MUST_FORCE_SAVE;
        }
        if (CORRUPTED_FORCE_SAVE.code == code) {
            return CORRUPTED_FORCE_SAVE;
        }
        return null;
    }
}
