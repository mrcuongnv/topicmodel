package com.longuh;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Yasser Ganjisaffar [lastname at gmail dot com]
 */

public class MultipleCrawlerController {
	private static Logger logger = LoggerFactory
			.getLogger(MultipleCrawlerController.class);

	public CrawlController SingleCrawlerController(String crawlStorageFolder, int numberOfThread, String name, String seed) throws Exception{
		CrawlConfig config1 = new CrawlConfig();
		config1.setCrawlStorageFolder(crawlStorageFolder+File.separator+name);
		config1.setPolitenessDelay(1000);
		config1.setMaxPagesToFetch(50);
		PageFetcher pageFetcher1 = new PageFetcher(config1);
		
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher1);

		CrawlController controller1 = new CrawlController(config1,
				pageFetcher1, robotstxtServer);
	
		controller1.setCustomData(seed);
		controller1.addSeed(seed);
		
		return controller1;
	}
	public static void main(String[] args) throws Exception {
				
//		if (args.length != 1) {
//			logger.info("Needed parameter: ");
//			logger.info("\t rootFolder (it will contain intermediate crawl data)");
//			return;
//		}

		/*
		 * crawlStorageFolder is a folder where intermediate crawl data is
		 * stored.
		 */
		// String crawlStorageFolder = args[0];
		String crawlStorageFolder = "data";

		CrawlConfig config1 = new CrawlConfig();
		CrawlConfig config2 = new CrawlConfig();

		config1.setProxyHost("10.61.213.140");
		config1.setProxyPort(3128);
		config2.setProxyHost("10.61.213.140");
		config2.setProxyPort(3128);
		/*
		 * The two crawlers should have different storage folders for their
		 * intermediate data
		 */
		config1.setCrawlStorageFolder(crawlStorageFolder + "/crawler1");
		config2.setCrawlStorageFolder(crawlStorageFolder + "/crawler2");

		config1.setPolitenessDelay(1000);
		config2.setPolitenessDelay(2000);

		config1.setMaxPagesToFetch(50);
		config2.setMaxPagesToFetch(100);

		/*
		 * We will use different PageFetchers for the two crawlers.
		 */
		PageFetcher pageFetcher1 = new PageFetcher(config1);
		PageFetcher pageFetcher2 = new PageFetcher(config2);

		/*
		 * We will use the same RobotstxtServer for both of the crawlers.
		 */
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig,
				pageFetcher1);

		CrawlController controller1 = new CrawlController(config1,
				pageFetcher1, robotstxtServer);
		CrawlController controller2 = new CrawlController(config2,
				pageFetcher2, robotstxtServer);

		String[] crawler1Domains = new String[] { "http://vnexpress.net"};
		String[] crawler2Domains = new String[] { "http://vnexpress.net" };

		controller1.setCustomData(crawler1Domains);
		controller2.setCustomData(crawler2Domains);

		controller1.addSeed("http://vnexpress.net/tin-tuc/thoi-su/nhieu-khu-vuc-ha-noi-ngap-nang-sau-mua-lon-3283063.html");
		controller2.addSeed("http://sohoa.vnexpress.net");

		/*
		 * The first crawler will have 5 concurrent threads and the second
		 * crawler will have 7 threads.
		 */
		controller1.startNonBlocking(BasicCrawler.class, 2);
//		controller2.startNonBlocking(BasicCrawler.class, 2);			
	    
		controller1.waitUntilFinish();
		logger.info("Crawler 1 is finished.");

		controller2.waitUntilFinish();
		logger.info("Crawler 2 is finished.");
		
	}
}