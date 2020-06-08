/**
 * Copyright  Vitalii Rudenskyi (vrudenskyi@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mckesson.kafka.connect.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.transforms.util.SimpleConfig;

public class ConfigUtils {

  public static final String MAP_KEY_CONFIG = "key";
  public static final String MAP_VALUE_CONFIG = "value";

  public static final ConfigDef MAP_ENTRY_CONFIG_DEF = new ConfigDef()
      .define(MAP_KEY_CONFIG, ConfigDef.Type.STRING, null, null, ConfigDef.Importance.HIGH, "Key, default value equeals to $alias")
      .define(MAP_VALUE_CONFIG, ConfigDef.Type.STRING, null, null, ConfigDef.Importance.HIGH, "Value object, required.");

  public static Map<String, String> getMap(AbstractConfig mapConfig, String mapKey) {

    Map<String, String> map = new HashMap<>();

    List<String> entries = mapConfig.getList(mapKey);
    for (String alias : entries) {
      SimpleConfig entryConfig = new SimpleConfig(MAP_ENTRY_CONFIG_DEF, mapConfig.originalsWithPrefix(mapKey + "." + alias + "."));

      String key = entryConfig.getString(MAP_KEY_CONFIG);
      if (StringUtils.isBlank(key)) {
        key = alias;
      }

      String value = entryConfig.getString(MAP_VALUE_CONFIG);
      if (value == null) {
        value = mapConfig.getString(mapKey + "." + alias);
      }
      map.put(key, value);
    }

    return map;
  }

}
