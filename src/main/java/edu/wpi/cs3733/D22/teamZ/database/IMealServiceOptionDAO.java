package edu.wpi.cs3733.D22.teamZ.database;

import edu.wpi.cs3733.D22.teamZ.entity.MealServiceOption;

import java.util.List;

public interface IMealServiceOptionDAO {

    //TODO: TEMPORARY FILE. FIX

    List<MealServiceOption> getAllMealServiceOptions();

    MealServiceOption getMealServiceOptionByID(String itemID);

    boolean addMealServiceOption(MealServiceOption option);

    boolean updateMealServiceOption(MealServiceOption option);

    boolean deleteMealServiceOption(MealServiceOption option);

    boolean exportToMealServiceOptionCSV();
}
