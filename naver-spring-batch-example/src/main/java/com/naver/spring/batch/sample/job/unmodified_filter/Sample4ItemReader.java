/*
Copyright 2018 NAVER Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.naver.spring.batch.sample.job.unmodified_filter;

import com.naver.spring.batch.sample.domain.Sample4;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class Sample4ItemReader implements ItemReader<Sample4> {
	private Iterator<Sample4> iterator;

	public Sample4ItemReader() {
		List<Sample4> samples = Arrays.asList(
				Sample4.of(1, "a", 1.1f, 10, "val1", null),
				Sample4.of(2, "b", 2.2f, 20, "val2", null),
				Sample4.of(3, "c", 3.3f, 30, "val3_", null),
				Sample4.of(4, "d", 4.4f, 40, "val4_", null),
				Sample4.of(5, "e", 5.5f, 50, "val5", null),
				Sample4.of(6, "f", 6.6f, 60, "val6", null),
				Sample4.of(7, "g", 7.7f, 70, "val7", null),
				Sample4.of(8, "h", 8.8f, 80, "val8_", null),
				Sample4.of(9, "i", 9.9f, 90, "val9_", null),
				Sample4.of(10, "j", 10.5f, 100, "val10", null)
		);

		iterator = samples.iterator();
	}

	@Override
	public Sample4 read() throws Exception {
		if (!iterator.hasNext())
			return null;

		Sample4 item = iterator.next();
		item.setUpdateTime(new Date());

		if (log.isDebugEnabled()) {
			log.debug("Read: {}", item);
		}

		return item;
	}
}
