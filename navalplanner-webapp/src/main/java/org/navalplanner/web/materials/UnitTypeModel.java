package org.navalplanner.web.materials;

import static org.navalplanner.web.I18nHelper._;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.commons.logging.LogFactory;
import org.navalplanner.business.common.exceptions.InstanceNotFoundException;
import org.navalplanner.business.common.exceptions.ValidationException;
import org.navalplanner.business.materials.daos.IUnitTypeDAO;
import org.navalplanner.business.materials.entities.UnitType;
import org.navalplanner.web.common.concurrentdetection.OnConcurrentModification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Model for the listing, creation and edition of UnitTypes
 *
 * @author Javier Moran Rua <jmoran@igalia.com>
 *
 */

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@OnConcurrentModification(goToPage = "/materials/unitTypes.zul")
public class UnitTypeModel implements IUnitTypeModel {

    private static final org.apache.commons.logging.Log LOG = LogFactory
    .getLog(UnitTypeModel.class);

    // This is the state of the model, just the current unitType being edited
    private UnitType unitTypeState;

    @Autowired
    private IUnitTypeDAO unitTypeDAO;

    @Override
    @Transactional(readOnly=true)
    public List<UnitType> getUnitTypes() {
        return unitTypeDAO.getAll();
    }

    @Override
    public void initCreate() {
        this.unitTypeState = UnitType.create();
    }

    @Override
    @Transactional(readOnly=true)
    public void initEdit(UnitType unitType) {
        Validate.notNull(unitType);
        this.unitTypeState = getFromDB(unitType);
    }

    private UnitType getFromDB(UnitType unitType) {
        try {
            return unitTypeDAO.find(unitType.getId());
        } catch (InstanceNotFoundException e) {
            LOG.error(_("It was not possible load entity. Not found. Id: " +
                    unitType.getId()), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public UnitType getCurrentUnitType() {
        return this.unitTypeState;
    }

    @Override
    @Transactional
    public void confirmSave() throws ValidationException {
        unitTypeDAO.save(this.unitTypeState);
    }

    @Override
    @Transactional(readOnly=true)
    public boolean existsAnotherUnitTypeWithName(String name) {
        boolean result;

        try {
            UnitType foundUnitType =
                unitTypeDAO.findByNameCaseInsensitive(name);
            result = isTheSameEntityAsState(foundUnitType) ? false : true;
        } catch (InstanceNotFoundException e) {
            result = false;
        }

        return result;
    }

    @Override
    @Transactional(readOnly=true)
    public boolean existsAnotherUnitTypeWithCode(String code) {
        boolean result;

        try {
            UnitType foundUnitType =
                unitTypeDAO.findByCode(code);
            result = isTheSameEntityAsState(foundUnitType) ? false : true;
        } catch (InstanceNotFoundException e) {
            result = false;
        }

        return result;
    }

    private boolean isTheSameEntityAsState(UnitType foundUnitType) {
        if (getCurrentUnitType().isNewObject()) {
            return false;
        } else {
            return foundUnitType.getId().equals(getCurrentUnitType().getId());
        }
    }

    @Override
    @Transactional(readOnly=true)
    public boolean isUnitTypeUsedInAnyMaterial(UnitType unitType) {
        return unitTypeDAO.isUnitTypeUsedInAnyMaterial(unitType);
    }

    @Override
    @Transactional
    public void remove(UnitType unitType) {
        try {
            unitTypeDAO.remove(unitType.getId());
        } catch (InstanceNotFoundException e) {
            LOG.error("Trying to remove unit type with id " + unitType.getId() +
                    " but it is not found in the database",e);
            throw new RuntimeException();
        }
    }

}