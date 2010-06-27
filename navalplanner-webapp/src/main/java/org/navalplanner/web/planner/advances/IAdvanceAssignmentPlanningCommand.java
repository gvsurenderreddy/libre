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

package org.navalplanner.web.planner.advances;

import org.navalplanner.business.planner.entities.TaskElement;
import org.navalplanner.web.planner.order.PlanningState;
import org.zkoss.ganttz.extensions.ICommandOnTask;

/**
 * Contract for {@link AdvanceAssignmentPlanningCommand}.
 * @author Susana Montes Pedreira <smontes@wirelessgalicia.com>
 */
public interface IAdvanceAssignmentPlanningCommand extends
        ICommandOnTask<TaskElement> {

    void initialize(
            AdvanceAssignmentPlanningController advanceAssignmentPlanningController,
            PlanningState state);

}