/*
 * This file is part of NavalPlan
 *
 * Copyright (C) 2009-2010 Fundación para o Fomento da Calidade Industrial e
 *                         Desenvolvemento Tecnolóxico de Galicia
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

package org.navalplanner.business.common.entities;

import static org.navalplanner.business.i18n.I18nHelper._;

/**
 * It represents the entities which use code generation
 * @author Susana Montes Pedreira <smontes@wirelessgalicia.com>
 */
public enum EntityNameEnum {
    ORDER(_("Order")), CRITERION(_("Criterion")), LABEL(_("Label")), MACHINE(
            _("Machine")), WORKER(_("Worker")), UNIT_TYPE(_("Unit type")), CALENDAR(
            _("Calendar"));

    private String description;

    private EntityNameEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}