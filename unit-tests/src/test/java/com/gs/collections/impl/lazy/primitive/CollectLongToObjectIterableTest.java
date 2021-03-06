/*
 * Copyright 2012 Goldman Sachs.
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

package com.gs.collections.impl.lazy.primitive;

import com.gs.collections.api.InternalIterable;
import com.gs.collections.api.LazyIterable;
import com.gs.collections.api.block.function.primitive.LongToObjectFunction;
import com.gs.collections.api.block.procedure.ObjectIntProcedure;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.block.procedure.Procedure2;
import com.gs.collections.impl.block.factory.PrimitiveFunctions;
import com.gs.collections.impl.block.factory.Procedures;
import com.gs.collections.impl.list.fixed.ArrayAdapter;
import com.gs.collections.impl.list.mutable.FastList;
import org.junit.Assert;
import org.junit.Test;

public class CollectLongToObjectIterableTest
{

    private <T> LazyIterable<T> newWith(T... elements)
    {
        return new CollectLongToObjectIterable(
                new CollectLongIterable(
                        ArrayAdapter.<T>adapt(elements).asLazy(),
                        PrimitiveFunctions.unboxNumberToLong()),
                new LongToObjectFunction()
                {
                    public T valueOf(long each)
                    {
                        return (T) Long.valueOf(each);
                    }
                });
    }

    @Test
    public void forEach()
    {
        InternalIterable<Long> select = this.newWith(1L, 2L, 3L, 4L, 5L);
        Appendable builder = new StringBuilder();
        Procedure<Long> appendProcedure = Procedures.append(builder);
        select.forEach(appendProcedure);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWithIndex()
    {
        InternalIterable<Long> select = this.newWith(1L, 2L, 3L, 4L, 5L);
        final StringBuilder builder = new StringBuilder("");
        select.forEachWithIndex(new ObjectIntProcedure<Long>()
        {
            public void value(Long object, int index)
            {
                builder.append(object);
                builder.append(index);
            }
        });
        Assert.assertEquals("1021324354", builder.toString());
    }

    @Test
    public void iterator()
    {
        InternalIterable<Long> select = this.newWith(1L, 2L, 3L, 4L, 5L);
        StringBuilder builder = new StringBuilder("");
        for (Long each : select)
        {
            builder.append(each);
        }
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void forEachWith()
    {
        InternalIterable<Long> select = this.newWith(1L, 2L, 3L, 4L, 5L);
        StringBuilder builder = new StringBuilder("");
        select.forEachWith(new Procedure2<Long, StringBuilder>()
        {
            public void value(Long each, StringBuilder aBuilder)
            {
                aBuilder.append(each);
            }
        }, builder);
        Assert.assertEquals("12345", builder.toString());
    }

    @Test
    public void selectInstancesOf()
    {
        Assert.assertEquals(
                FastList.<Long>newListWith(1L, 2L, 3L, 4L, 5L),
                this.newWith(1, 2L, 3, 4L, 5).selectInstancesOf(Long.class).toList());
    }
}
