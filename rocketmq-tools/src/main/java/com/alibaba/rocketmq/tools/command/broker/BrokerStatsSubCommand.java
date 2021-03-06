/**
 * Copyright (C) 2010-2013 Alibaba Group Holding Limited
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
 */
package com.alibaba.rocketmq.tools.command.broker;

import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import com.alibaba.rocketmq.common.protocol.body.KVTable;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.alibaba.rocketmq.tools.command.SubCommand;


/**
 * 获取Broker运行时统计信息
 * 
 * @author shijia.wxr<vintage.wang@gmail.com>
 * @since 2013-8-14
 */
public class BrokerStatsSubCommand implements SubCommand {

    @Override
    public String commandName() {
        return "brokerStats";
    }


    @Override
    public String commandDesc() {
        return "Fetch broker runtime stats data";
    }


    @Override
    public Options buildCommandlineOptions(Options options) {
        Option opt = new Option("b", "brokerAddr", true, "create topic to which broker");
        opt.setRequired(true);
        options.addOption(opt);

        return options;
    }


    @Override
    public void execute(CommandLine commandLine, Options options) {
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();

        defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));

        try {
            defaultMQAdminExt.start();

            String brokerAddr = commandLine.getOptionValue('b');

            KVTable kvTable = defaultMQAdminExt.fetchBrokerRuntimeStats(brokerAddr);

            Iterator<Entry<String, String>> it = kvTable.getTable().entrySet().iterator();
            while (it.hasNext()) {
                Entry<String, String> next = it.next();
                System.out.printf("%-32s: %s\n", next.getKey(), next.getValue());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }
}
