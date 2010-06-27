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

package org.navalplanner.web.scenarios;

import static org.navalplanner.web.I18nHelper._;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.navalplanner.business.common.exceptions.ValidationException;
import org.navalplanner.business.orders.entities.Order;
import org.navalplanner.business.scenarios.IScenarioManager;
import org.navalplanner.business.scenarios.bootstrap.PredefinedScenarios;
import org.navalplanner.business.scenarios.entities.Scenario;
import org.navalplanner.web.common.IMessagesForUser;
import org.navalplanner.web.common.ITemplateModel;
import org.navalplanner.web.common.Level;
import org.navalplanner.web.common.MessagesForUser;
import org.navalplanner.web.common.OnlyOneVisible;
import org.navalplanner.web.common.Util;
import org.navalplanner.web.common.ITemplateModel.IOnFinished;
import org.navalplanner.web.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.SimpleTreeNode;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;
import org.zkoss.zul.api.Window;

/**
 * Controller for CRUD actions over a {@link Scenario}.
 *
 * @author Manuel Rego Casasnovas <mrego@igalia.com>
 */
public class ScenarioCRUDController extends GenericForwardComposer {

    private static final Log LOG = LogFactory
            .getLog(ScenarioCRUDController.class);

    @Autowired
    private IScenarioModel scenarioModel;

    @Autowired
    private ITemplateModel templateModel;

    @Autowired
    private IScenarioManager scenarioManager;

    private Window listWindow;

    private Window createWindow;

    private Window editWindow;

    private OnlyOneVisible visibility;

    private IMessagesForUser messagesForUser;

    private Component messagesContainer;

    private ScenariosTreeitemRenderer scenariosTreeitemRenderer = new ScenariosTreeitemRenderer();

    public Scenario getScenario() {
        return scenarioModel.getScenario();
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        messagesForUser = new MessagesForUser(messagesContainer);
        comp.setVariable("scenarioController", this, true);
        getVisibility().showOnly(listWindow);
    }

    public void cancel() {
        scenarioModel.cancel();
        goToList();
    }

    public void goToList() {
        Util.reloadBindings(listWindow);
        getVisibility().showOnly(listWindow);
    }

    public void goToEditForm(Scenario scenario) {
        scenarioModel.initEdit(scenario);
        getVisibility().showOnly(editWindow);
        Util.reloadBindings(editWindow);
    }

    public void save() {
        try {
            scenarioModel.confirmSave();
            messagesForUser.showMessage(Level.INFO, _("Scenario \"{0}\" saved",
                    scenarioModel.getScenario().getName()));
            goToList();
        } catch (ValidationException e) {
            messagesForUser.showInvalidValues(e);
        }
    }

    private OnlyOneVisible getVisibility() {
        if (visibility == null) {
            visibility = new OnlyOneVisible(listWindow, createWindow,
                    editWindow);
        }
        return visibility;
    }

    public void goToCreateDerivedForm(Scenario scenario) {
        scenarioModel.initCreateDerived(scenario);
        getVisibility().showOnly(createWindow);
        Util.reloadBindings(createWindow);
    }

    public ScenariosTreeModel getScenariosTreeModel() {
        return new ScenariosTreeModel(new ScenarioTreeRoot(scenarioModel
                .getScenarios()));
    }

    public ScenariosTreeitemRenderer getScenariosTreeitemRenderer() {
        return scenariosTreeitemRenderer;
    }

    public class ScenariosTreeitemRenderer implements TreeitemRenderer {

        @Override
        public void render(final Treeitem item, Object data) throws Exception {
            SimpleTreeNode simpleTreeNode = (SimpleTreeNode) data;
            final Scenario scenario = (Scenario) simpleTreeNode.getData();
            item.setValue(data);

            Scenario currentScenario = scenarioManager.getCurrent();
            boolean isCurrentScenario = currentScenario.getId().equals(
                    scenario.getId());

            Treerow treerow = new Treerow();

            Treecell nameTreecell = new Treecell();
            Label nameLabel = new Label(scenario.getName());
            nameTreecell.appendChild(nameLabel);
            treerow.appendChild(nameTreecell);

            Treecell operationsTreecell = new Treecell();

            Button createDerivedButton = new Button();
            createDerivedButton.setTooltiptext(_("Create derived"));
            createDerivedButton.setSclass("icono");
            createDerivedButton.setImage("/common/img/ico_derived1.png");
            createDerivedButton.setHoverImage("/common/img/ico_derived.png");

            createDerivedButton.addEventListener(Events.ON_CLICK,
                    new EventListener() {

                @Override
                public void onEvent(Event event) throws Exception {
                    goToCreateDerivedForm(scenario);
                }

            });
            operationsTreecell.appendChild(createDerivedButton);

            Button editButton = Util.createEditButton(new EventListener() {

                @Override
                public void onEvent(Event event) throws Exception {
                    goToEditForm(scenario);
                }

            });
            operationsTreecell.appendChild(editButton);

            Button removeButton = Util.createRemoveButton(new EventListener() {

                @Override
                public void onEvent(Event event) throws Exception {
                    scenarioModel.remove(scenario);
                    Util.reloadBindings(listWindow);
                }

            });

            boolean isMainScenario = PredefinedScenarios.MASTER.getScenario()
                    .getId().equals(scenario.getId());
            List<Scenario> derivedScenarios = scenarioModel
                    .getDerivedScenarios(scenario);
            if (isCurrentScenario || isMainScenario
                    || !derivedScenarios.isEmpty()) {
                removeButton.setDisabled(true);
            }
            operationsTreecell.appendChild(removeButton);

            Button connectButton = new Button(_("Connect"));
            connectButton.addEventListener(Events.ON_CLICK,
                    new EventListener() {

                        @Override
                        public void onEvent(Event event) throws Exception {
                            connectTo(scenario);
                        }

                        private void connectTo(Scenario scenario) {
                            templateModel.setScenario(SecurityUtils
                                    .getSessionUserLoginName(),
                                    scenario,
                                    new IOnFinished() {
                                        @Override
                                        public void onWithoutErrorFinish() {
                                            Executions
                                                    .sendRedirect("/scenarios/scenarios.zul");
                                        }

                                        @Override
                                        public void errorHappened(
                                                Exception exceptionHappened) {
                                            errorHappenedDoingReassignation(exceptionHappened);
                                        }
                                    });
                        }

                    });
            if (isCurrentScenario) {
                connectButton.setDisabled(true);
            }
            operationsTreecell.appendChild(connectButton);

            treerow.appendChild(operationsTreecell);

            item.appendChild(treerow);

            // Show the tree expanded at start
            item.setOpen(true);
        }

    }

    private void errorHappenedDoingReassignation(Exception exceptionHappened) {
        LOG.error("error happened doing reassignation", exceptionHappened);
        messagesForUser.showMessage(Level.ERROR, _(
                "error doing reassignation: {0}", exceptionHappened));
    }

    public Set<Order> getOrders() {
        Scenario scenario = scenarioModel.getScenario();
        if (scenario == null) {
            return Collections.emptySet();
        }
        return scenario.getOrders().keySet();
    }

}