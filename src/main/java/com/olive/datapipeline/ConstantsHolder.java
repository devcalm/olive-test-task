package com.olive.datapipeline;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstantsHolder {
    public static final String DETECTION_BASE_PATH = "api/detection";
    public static final String DETECTION_SAVE_TOPIC = "detection-save";
    public static final String DETECTION_DLQ_TOPIC = "detection-dlq";
    public static final String DETECTION_GROUP_ID = "detection-group";
}
