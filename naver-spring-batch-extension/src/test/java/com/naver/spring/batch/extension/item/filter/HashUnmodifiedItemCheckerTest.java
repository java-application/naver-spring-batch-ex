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
package com.naver.spring.batch.extension.item.filter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class HashUnmodifiedItemCheckerTest {
	private final List<ItemHash> storedItemHash = new ArrayList<>();

	@Mock
	private HashRepository hashRepository;

	@Before
	public void before() {
		//Mock for hashRepository.saveItemHashes()
		Mockito.doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			storedItemHash.addAll((List)args[0]);
			return null;
		}).when(hashRepository).saveItemHashes(Matchers.anyListOf(ItemHash.class));

		//Mock for hashRepository.getHashValue()
		Mockito.doAnswer(invocation -> {
			Object[] args = invocation.getArguments();
			String hashKey = (String)args[0];

			for (ItemHash itemHash : storedItemHash) {
				if (hashKey.equals(itemHash.getItemKey())) {
					return itemHash.getItemHash();
				}
			}

			return null;
		}).when(hashRepository).getHashValue(Matchers.anyString());
	}

	@Test
	public void check() throws Exception {
		HashUnmodifiedItemChecker<TestObj> checker = new HashUnmodifiedItemChecker<>();
		checker.setHashRepository(hashRepository);
		checker.setKeyPropertyNames(Arrays.asList("name"));

		checker.setIgnorePropertyNames(Arrays.asList("randomVal", "randomVal2"));
		checker.afterPropertiesSet();

		TestObj p1 = new TestObj();
		p1.setId(1);
		p1.setName("YK");
		p1.setAge(20);

		p1.setRandomVal(123);
		p1.setRandomVal2(678);

		TestObj p2 = new TestObj();
		p2.setId(1);
		p2.setName("YK");
		p2.setAge(20);

		p2.setRandomVal(345);
		p2.setRandomVal2(493);

		boolean p1UnModified = checker.check(p1);
		checker.afterChunk(null);

		boolean p2UnModified = checker.check(p2);
		checker.afterChunk(null);

		Assert.assertFalse("p1UnModified must be false", p1UnModified);
		Assert.assertTrue("p2UnModified must be true",p2UnModified);
	}
}
