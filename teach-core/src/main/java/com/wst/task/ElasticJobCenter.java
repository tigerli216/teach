/*
 * ElasticJobCenter.java Created on 2017年10月18日 下午5:28:02
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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;

/**
 * Elastic-Job调度中心
 * 
 * @author <a href="mailto:lijw@hiwitech.com">lijiawen</a>
 * @version 1.0
 */
@Configuration
public class ElasticJobCenter {

	@Resource
	private ZookeeperRegistryCenter regCenter;

	@Resource
	private Jobs jobs;

	/**
	 * 任务调度初始化
	 */
	@PostConstruct
	public void jobInit() {
		new SpringJobScheduler(jobs.new ClosePayJob(), regCenter,
				getSimpleJobConfiguration(Jobs.ClosePayJob.class, "0 */10 * * * ?", 1, "", "关闭订单任务")).init();

		// new SpringJobScheduler(jobs.new CustomClassVideoJob(), regCenter,
		// getSimpleJobConfiguration(Jobs.CustomClassVideoJob.class, "0 */1 * *
		// * ?", 1, "", "根据视频vid获取视频地址任务")).init();

		new SpringJobScheduler(jobs.new CustomClass72HourNotConfirm(), regCenter, getSimpleJobConfiguration(
				Jobs.CustomClass72HourNotConfirm.class, "0 */10 * * * ?", 1, "", "72小时或24小时未确认课时推送消息任务")).init();

		new SpringJobScheduler(jobs.new CustomClassPlanTime2Day(), regCenter, getSimpleJobConfiguration(
				Jobs.CustomClassPlanTime2Day.class, "0 0 8 * * ?", 1, "", "离计划上课还有2天或当天未约课推送消息")).init();

		/*
		 * new SpringJobScheduler(jobs.new CustomClass2WeekNotClass(),
		 * regCenter, getSimpleJobConfiguration(
		 * Jobs.CustomClass2WeekNotClass.class, "0 0 8 * * ?", 1, "",
		 * "2周未上课提醒导师推送消息")).init();
		 */

		new SpringJobScheduler(jobs.new CustomClassDataTime2Hour(), regCenter, getSimpleJobConfiguration(
				Jobs.CustomClassDataTime2Hour.class, "0 */10 * * * ?", 1, "", "约课时间到前2小时提醒上课推送消息")).init();

		new SpringJobScheduler(jobs.new PublicClassBeginTime2Hour(), regCenter, getSimpleJobConfiguration(
				Jobs.PublicClassBeginTime2Hour.class, "0 */10 * * * ?", 1, "", "离上课还有2小时的直播课推送消息")).init();

		new SpringJobScheduler(jobs.new CloseCustomPayJob(), regCenter,
				getSimpleJobConfiguration(Jobs.CloseCustomPayJob.class, "0 0 8 * * ?", 1, "", "关闭定制课订单任务")).init();

		new SpringJobScheduler(jobs.new CustomClassAfterPlanTime2Day(), regCenter, getSimpleJobConfiguration(
				Jobs.CustomClassAfterPlanTime2Day.class, "0 0 8 * * ?", 1, "", "超过计划上课2天未约课推送消息")).init();

		new SpringJobScheduler(jobs.new UpdatePublicStatusJob(), regCenter,
				getSimpleJobConfiguration(Jobs.UpdatePublicStatusJob.class, "0 */3 * * * ?", 1, "", "更新直播课课时状态"))
						.init();

		new SpringJobScheduler(jobs.new UpdateUSerLevel(), regCenter,
				getSimpleJobConfiguration(Jobs.UpdateUSerLevel.class, "0 */3 * * * ?", 1, "", "更新学生等级")).init();

	}

	/**
	 * 创建简单作业配置构建器
	 * 
	 * @param jobClass
	 * @param cron
	 *            cron表达式
	 * @param shardingTotalCount
	 *            分片总数
	 * @param shardingItemParameters
	 *            分片参数
	 * @return
	 */
	private LiteJobConfiguration getSimpleJobConfiguration(final Class<? extends SimpleJob> jobClass, final String cron,
			final int shardingTotalCount, final String shardingItemParameters, final String description) {
		return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(
				JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
						.shardingItemParameters(shardingItemParameters).description(description).build(),
				jobClass.getCanonicalName())).overwrite(true).build();
	}

	/**
	 * 创建数据流作业配置构建器
	 * 
	 * @param jobClass
	 * @param cron
	 *            cron表达式
	 * @param shardingTotalCount
	 *            分片总数
	 * @param shardingItemParameters
	 *            分片参数
	 * @param description
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private LiteJobConfiguration getDataflowJobConfiguration(final Class<? extends DataflowJob> jobClass,
			final String cron, final int shardingTotalCount, final String shardingItemParameters,
			final String description) {
		return LiteJobConfiguration.newBuilder(new DataflowJobConfiguration(
				JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
						.shardingItemParameters(shardingItemParameters).description(description).build(),
				jobClass.getCanonicalName(), true)).overwrite(true).build();
	}

	@Bean(initMethod = "init")
	public ZookeeperRegistryCenter regCenter(@Value("${elastic.regCenter.serverList}") final String serverList,
			@Value("${elastic.regCenter.namespace}") final String namespace) {
		ZookeeperRegistryCenter regCenter = new ZookeeperRegistryCenter(
				new ZookeeperConfiguration(serverList, namespace));

		return regCenter;
	}

}
