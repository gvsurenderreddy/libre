package org.navalplanner.web.materials;

import java.util.List;

import org.navalplanner.business.common.exceptions.ValidationException;
import org.navalplanner.business.materials.entities.UnitType;

/**
 * Interface for the model which lets the client of this model
 * list the unit types, create new unit types, edit existing unit types
 * and remove unity types
 *
 * <strong>Conversation state:</strong> A unit type being edited or created.
 *
 * <strong>Not conversational methods:</strong>
 * <ul>
 *  <li>getUnitTypes</li>
 *  <li>existsAnotherUnitTypeWithName</li>
 *  <li>existsAnotherUnitTypeWithCode</li>
 *  <li>isUnitTypeUsedInAnyMaterial</li>
 *  <li>isUnitTypeUsedInAnyMaterial</li>
 *  <li>remove</li>
 * </ul>
 *
 * <strong>Conversational methods:</strong>
 * <ul>
 *   <li>initCreate</li>
 *   <li>initEdit</li>
 *   <li>getCurrentUnitType</li>
 *   <li>confirmSave</li>
 * </ul>
 *
 * @author Javier Moran Rua <jmoran@igalia.com>
 */

public interface IUnitTypeModel {

    // Non conversational methods

    /**
     * Query the database to get all the unit types in the database
     */
    List<UnitType> getUnitTypes();

    /**
     * This method check if there is another UnitType in the database
     * different from the one in the state of the model which had the same
     * measure name as the parameter.
     *
     * @param name the measure name to be checked as unique in the unit types
     * @return     the boolean with the result
     */
    boolean existsAnotherUnitTypeWithName(String name);

    /**
     * This method check if there is another UnitType in the database
     * different from the one in the state of the model which had teh same
     * code as the parameter
     *
     * @param code the code to be checked as unique
     * @return     the boolean showing the result
     */
    boolean existsAnotherUnitTypeWithCode(String code);

    /**
     * This method finds out if the unit type passed as parameter is
     * used to measure any material
     *
     * @param unitType the unitType to check
     * @return         the boolean with the result
     */
    boolean isUnitTypeUsedInAnyMaterial(UnitType unitType);

    /**
     * This method removes the unit type passed as parameter from the
     * database
     *
     * @param unitType the unitType which is wanted to be deleted
     */
    void remove(UnitType unitType);

    //Conversational methods

    /**
     * First method of the conversational state. Prepares the state with the
     * unit type to edit
     */
    void initEdit(UnitType unitType);

    /**
     * First method of the conversational state. Creates an empty unit type
     * to be saved
     */
    void initCreate();

    /**
     * Get the current unit type which is in the state of the
     *
     * @return
     */
    UnitType getCurrentUnitType();

    /**
     * Last method of the conversation. It ends with the saving of the unit
     * type in the state to the database
     *
     * @throws ValidationException
     */
    void confirmSave() throws ValidationException;

}