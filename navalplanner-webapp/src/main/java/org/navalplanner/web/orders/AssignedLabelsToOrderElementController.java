package org.navalplanner.web.orders;

import static org.navalplanner.web.I18nHelper._;

import java.util.ArrayList;
import java.util.List;

import org.navalplanner.business.labels.entities.Label;
import org.navalplanner.business.labels.entities.LabelType;
import org.navalplanner.business.orders.entities.OrderElement;
import org.navalplanner.web.common.Util;
import org.navalplanner.web.common.components.Autocomplete;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 * Controller for showing OrderElement assigned labels
 *
 * @author Diego Pino García <dpino@igalia.com>
 *
 */
public class AssignedLabelsToOrderElementController extends
        GenericForwardComposer {

    private IAssignedLabelsToOrderElementModel assignedLabelsToOrderElementModel;

    private Window window;

    private Autocomplete cbLabelType;

    private Grid directLabels;

    private Textbox txtLabelName;

    private Bandbox bdLabels;

    private Listbox lbLabels;

    private LabelRenderer labelRenderer = new LabelRenderer();

    public OrderElement getOrderElement() {
        return assignedLabelsToOrderElementModel.getOrderElement();
    }

    public void setOrderElement(OrderElement orderElement) {
        assignedLabelsToOrderElementModel.setOrderElement(orderElement);
    }

    public void openWindow(IOrderElementModel orderElementModel) {
        assignedLabelsToOrderElementModel.init(orderElementModel
                .getOrderElement());
        Util.reloadBindings(self);
    }

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp.getFellow("listOrderElementLabels"));
        comp.setVariable("assignedLabelsController", this, true);
        window = (Window) comp;

        // Configure bandbox with all labels
        final List<Label> allLabels = getAllLabels();
        bdLabels.setVariable("allLabels", allLabels, true);
        lbLabels.setModel(new SimpleListModel(allLabels));

        // Initialize selectedLabel
        bdLabels.setVariable("selectedLabel", null, true);

        // Set autodrop
        bdLabels.setAutodrop(true);
    }

    /**
     * On selecting one {@link Label} from bandbox listbox
     *
     * Selected {@link Label} is stored in an internal {@link Bandbox} variable:
     * selectedLabel,
     *
     * @param event
     */
    public void onSelectLabel(Event event) {
        Listitem listitem = (Listitem) lbLabels.getSelectedItems().iterator()
                .next();
        Label label = (Label) listitem.getValue();
        bdLabels.setValue(label.getName());
        bdLabels.setVariable("selectedLabel", label, true);
        bdLabels.close();
    }

    /**
     * Search {@link Label} starting with input text
     *
     * @param event
     */
    public void onSearchLabels(InputEvent event) {
        bdLabels.setVariable("selectedLabel", null, true);
        List<Label> filteredLabels = labelsStartWith(event.getValue());
        lbLabels.setModel(new SimpleListModel(filteredLabels));
        lbLabels.invalidate();
    }

    @SuppressWarnings("unchecked")
    private List<Label> labelsStartWith(String prefix) {
        List<Label> result = new ArrayList<Label>();
        List<Label> labels = (List<Label>) bdLabels.getVariable("allLabels",
                true);
        for (Label label : labels) {
            if (label.getName().startsWith(prefix)) {
                result.add(label);
            }
        }
        return result;
    }

    /**
     * Executed on pressing Assign button
     *
     * Adds selected label to direct labels list
     *
     */
    public void onAssignLabel() {
        Label label = (Label) bdLabels.getVariable("selectedLabel", true);
        if (label == null) {
            throw new WrongValueException(bdLabels, _("cannot be null"));
        }
        if (isAssigned(label)) {
            throw new WrongValueException(bdLabels, _("already assigned"));
        }
        assignLabel(label);
        clear(bdLabels);
    }

    @SuppressWarnings("unchecked")
    private void clear(Bandbox bandbox) {
        final List<Label> labels = (List<Label>) bandbox.getVariable(
                "allLabels", true);
        lbLabels.setModel(new SimpleListModel(labels));
    }

    /**
     * Executed on pressing createAndAssign button
     *
     * Creates a new label for a type, in case it does not exist, and added it
     * to the list of direct labels
     *
     */
    public void onCreateAndAssign() {
        // Check LabelType is not null
        final Comboitem comboitem = cbLabelType.getSelectedItem();
        if (comboitem == null || comboitem.getValue() == null) {
            throw new WrongValueException(cbLabelType,
                    _("please, select an item"));
        }

        // Check Label is not null or empty
        final String labelName = txtLabelName.getValue();
        if (labelName == null || labelName.isEmpty()) {
            throw new WrongValueException(txtLabelName,
                    _("cannot be null or empty"));
        }

        // Label does not exist, create
        final LabelType labelType = (LabelType) comboitem.getValue();
        Label label = assignedLabelsToOrderElementModel
                .findLabelByNameAndType(labelName, labelType);
        if (label == null) {
            label = createLabel(labelName, labelType);
        } else {
            // Label is already assigned?
            if (isAssigned(label)) {
                throw new WrongValueException(txtLabelName,
                        _("already assigned"));
            }
        }
        assignLabel(label);
        clear(txtLabelName);
    }

    private void clear(Textbox textbox) {
        textbox.setValue("");
    }

    private void assignLabel(Label label) {
        assignedLabelsToOrderElementModel.assignLabel(label);
        Util.reloadBindings(directLabels);
    }

    private boolean isAssigned(Label label) {
        return assignedLabelsToOrderElementModel.isAssigned(label);
    }

    private Label createLabel(String labelName, LabelType labelType) {
        return assignedLabelsToOrderElementModel.createLabel(labelName,
                labelType);
    }

    public void deleteLabel(Label label) {
        assignedLabelsToOrderElementModel.deleteLabel(label);
        Util.reloadBindings(directLabels);
    }

    public List<Label> getLabels() {
        return assignedLabelsToOrderElementModel.getLabels();
    }

    public List<Label> getInheritedLabels() {
        return assignedLabelsToOrderElementModel.getInheritedLabels();
    }

    public List<Label> getAllLabels() {
        return assignedLabelsToOrderElementModel.getAllLabels();
    }

    /**
     * Undo changes and close window
     *
     * @param event
     */
    public void onClose(Event event) {
        cancel();
        close();
        event.stopPropagation();
    }

    private void cancel() {
        assignedLabelsToOrderElementModel.cancel();
    }

    private void close() {
        window.setVisible(false);
    }

    public ListitemRenderer getLabelRenderer() {
        return labelRenderer;
    }

    /**
     * Render for {@link Label}
     *
     * @author Diego Pino Garcia <dpino@igalia.com>
     */
    private class LabelRenderer implements ListitemRenderer {

        @Override
        public void render(Listitem item, Object data) throws Exception {
            Label label = (Label) data;

            item.setValue(data);

            final Listcell labelType = new Listcell();
            labelType.setLabel(label.getType().getName());
            labelType.setParent(item);

            final Listcell labelName = new Listcell();
            labelName.setLabel(label.getName());
            labelName.setParent(item);
        }
    }

}
