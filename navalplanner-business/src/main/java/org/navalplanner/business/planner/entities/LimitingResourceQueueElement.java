/*
 * This file is part of NavalPlan
 *
 * Copyright (C) 2009 Fundación para o Fomento da Calidade Industrial e
 *                    Desenvolvemento Tecnolóxico de Galicia
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.navalplanner.business.planner.entities;

import static org.navalplanner.business.i18n.I18nHelper._;

import java.util.Date;

import org.apache.commons.lang.Validate;
import org.joda.time.LocalDate;
import org.navalplanner.business.common.BaseEntity;
import org.navalplanner.business.resources.entities.LimitingResourceQueue;

/**
 *
 * @author Diego Pino Garcia <dpino@igalia.com>
 *
 */
public class LimitingResourceQueueElement extends BaseEntity {

    private ResourceAllocation<?> resourceAllocation;

    private LimitingResourceQueue limitingResourceQueue;

    private Date earlierStartDateBecauseOfGantt;

    private QueuePosition startQueuePosition;

    private QueuePosition endQueuePosition;

    public Date getEarlierStartDateBecauseOfGantt() {
        return earlierStartDateBecauseOfGantt;
    }

    public void setEarlierStartDateBecauseOfGantt(
            Date earlierStartDateBecauseOfGantt) {
        this.earlierStartDateBecauseOfGantt = earlierStartDateBecauseOfGantt;
    }

    public static LimitingResourceQueueElement create() {
        return create(new LimitingResourceQueueElement());
    }

    protected LimitingResourceQueueElement() {

    }

    public ResourceAllocation<?> getResourceAllocation() {
        return resourceAllocation;
    }

    public void setResourceAllocation(ResourceAllocation<?> resourceAllocation) {
        this.resourceAllocation = resourceAllocation;
    }

    public LimitingResourceQueue getLimitingResourceQueue() {
        return limitingResourceQueue;
    }

    public void setLimitingResourceQueue(LimitingResourceQueue limitingResourceQueue) {
        this.limitingResourceQueue = limitingResourceQueue;
    }

    public LocalDate getStartDate() {
        return startQueuePosition.getDate();
    }

    public void setStartDate(LocalDate date) {
        startQueuePosition.setDate(date);
    }

    public int getStartHour() {
        return startQueuePosition.getHour();
    }

    public void setStartHour(int hour) {
        startQueuePosition.setHour(hour);
    }

    public LocalDate getEndDate() {
        return endQueuePosition.getDate();
    }

    public void setEndDate(LocalDate date) {
        endQueuePosition.setDate(date);
    }

    public int getEndHour() {
        return endQueuePosition.getHour();
    }

    public void setEndHour(int hour) {
        endQueuePosition.setHour(hour);
    }

}