/*
 * SimpleTestJob.java Created on 2017年10月18日 下午6:08:51
 * Copyright (c) 2017 HeWei Technology Co.Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wst.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;

/**
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public class SimpleTestJob implements SimpleJob {
    
    private static HiwiLog log = HiwiLogFactory.getLogger(SimpleTestJob.class);

    @Override
    public void execute(ShardingContext context) {
        
        log.debug(String.format("Item: %s | Time: %s | Thread: %s | %s", context.getShardingItem(),
                new SimpleDateFormat("HH:mm:ss").format(new Date()), Thread.currentThread().getId(), "SIMPLE"));

    }

}
