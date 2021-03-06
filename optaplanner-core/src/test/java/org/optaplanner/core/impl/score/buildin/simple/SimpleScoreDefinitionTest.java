/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
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

package org.optaplanner.core.impl.score.buildin.simple;

import org.junit.jupiter.api.Test;
import org.optaplanner.core.api.score.buildin.simple.SimpleScore;
import org.optaplanner.core.config.score.trend.InitializingScoreTrendLevel;
import org.optaplanner.core.impl.score.trend.InitializingScoreTrend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class SimpleScoreDefinitionTest {

    @Test
    public void getZeroScore() {
        SimpleScore score = new SimpleScoreDefinition().getZeroScore();
        assertThat(score).isEqualTo(SimpleScore.ZERO);
    }

    @Test
    public void getSoftestOneScore() {
        SimpleScore score = new SimpleScoreDefinition().getOneSoftestScore();
        assertThat(score).isEqualTo(SimpleScore.ONE);
    }

    @Test
    public void getLevelsSize() {
        assertEquals(1, new SimpleScoreDefinition().getLevelsSize());
    }

    @Test
    public void getLevelLabels() {
        assertArrayEquals(new String[]{"score"}, new SimpleScoreDefinition().getLevelLabels());
    }

    @Test
    public void buildOptimisticBoundOnlyUp() {
        SimpleScoreDefinition scoreDefinition = new SimpleScoreDefinition();
        SimpleScore optimisticBound = scoreDefinition.buildOptimisticBound(
                InitializingScoreTrend.buildUniformTrend(InitializingScoreTrendLevel.ONLY_UP, 1),
                SimpleScore.of(-1));
        assertEquals(0, optimisticBound.getInitScore());
        assertEquals(Integer.MAX_VALUE, optimisticBound.getScore());
    }

    @Test
    public void buildOptimisticBoundOnlyDown() {
        SimpleScoreDefinition scoreDefinition = new SimpleScoreDefinition();
        SimpleScore optimisticBound = scoreDefinition.buildOptimisticBound(
                InitializingScoreTrend.buildUniformTrend(InitializingScoreTrendLevel.ONLY_DOWN, 1),
                SimpleScore.of(-1));
        assertEquals(0, optimisticBound.getInitScore());
        assertEquals(-1, optimisticBound.getScore());
    }

    @Test
    public void buildPessimisticBoundOnlyUp() {
        SimpleScoreDefinition scoreDefinition = new SimpleScoreDefinition();
        SimpleScore pessimisticBound = scoreDefinition.buildPessimisticBound(
                InitializingScoreTrend.buildUniformTrend(InitializingScoreTrendLevel.ONLY_UP, 1),
                SimpleScore.of(-1));
        assertEquals(0, pessimisticBound.getInitScore());
        assertEquals(-1, pessimisticBound.getScore());
    }

    @Test
    public void buildPessimisticBoundOnlyDown() {
        SimpleScoreDefinition scoreDefinition = new SimpleScoreDefinition();
        SimpleScore pessimisticBound = scoreDefinition.buildPessimisticBound(
                InitializingScoreTrend.buildUniformTrend(InitializingScoreTrendLevel.ONLY_DOWN, 1),
                SimpleScore.of(-1));
        assertEquals(0, pessimisticBound.getInitScore());
        assertEquals(Integer.MIN_VALUE, pessimisticBound.getScore());
    }

    @Test
    public void divideBySanitizedDivisor() {
        SimpleScoreDefinition scoreDefinition = new SimpleScoreDefinition();
        SimpleScore dividend = scoreDefinition.fromLevelNumbers(2, new Number[] {10});
        SimpleScore zeroDivisor = scoreDefinition.getZeroScore();
        assertThat(scoreDefinition.divideBySanitizedDivisor(dividend, zeroDivisor))
                .isEqualTo(dividend);
        SimpleScore oneDivisor = scoreDefinition.getOneSoftestScore();
        assertThat(scoreDefinition.divideBySanitizedDivisor(dividend, oneDivisor))
                .isEqualTo(dividend);
        SimpleScore tenDivisor = scoreDefinition.fromLevelNumbers(10, new Number[] {10});
        assertThat(scoreDefinition.divideBySanitizedDivisor(dividend, tenDivisor))
                .isEqualTo(scoreDefinition.fromLevelNumbers(0, new Number[] {1}));
    }

}
