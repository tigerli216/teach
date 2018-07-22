/*
 * MyDataflowJob.java Created on 2017年10月18日 下午6:10:50
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

import java.util.List;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.hiwi.common.log.HiwiLog;
import com.hiwi.common.log.HiwiLogFactory;

/**
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
public class MyDataflowJob implements DataflowJob<String> {

    private static HiwiLog log = HiwiLogFactory.getLogger(MyDataflowJob.class);

    @Override
    public List<String> fetchData(ShardingContext context) {
        log.debug("=========dataflow job 执行======");
        switch (context.getShardingItem()) {
            case 0:
                List<String> data = null;// get data from database by sharding
                                         // item 0
                return data;
            case 1:
                List<String> data1 = null; // get data from database by sharding
                                           // item 1
                return data1;
            case 2:
                List<String> data2 = null; // get data from database by sharding
                                           // item 2
                return data2;
            // case n: ...
        }
        return null;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<String> data) {
        // process data
        // ...
    }

}
