package edu.wpi.cs3733.D22.teamZ.entity;

import java.util.List;

/**
 * A MealItem is an item such as a drink, entrée, or snack. It is used with the Meal Service Request
 * to offer items within these three categories with attributes such as name, what time of day
 * offered (e.g.: breakfast/lunch/dinner), and any major food allergens associated with them.
 */
public class MealItem {
  private String mealItemID;
  private String name;
  private String category;
  private int calories;
  private List<String> timeOfDayList; // Test
  private List<String> daysOfWeekList;
  private List<String> allergensList;

  public MealItem() {}

  public MealItem(
      String mealItemID,
      String name,
      String category,
      int calories,
      List<String> timeOfDayList,
      List<String> daysOfWeekList,
      List<String> allergensList) {
    this.mealItemID = mealItemID;
    this.name = name;
    this.category = category;
    this.calories = calories;
    this.timeOfDayList = timeOfDayList;
    this.daysOfWeekList = daysOfWeekList;
    this.allergensList = allergensList;
  }

  public MealItem(
      String name,
      String category,
      int calories,
      List<String> timeOfDayList,
      List<String> daysOfWeekList,
      List<String> allergensList) {
    this.name = name;
    this.category = category;
    this.calories = calories;
    this.timeOfDayList = timeOfDayList;
    this.daysOfWeekList = daysOfWeekList;
    this.allergensList = allergensList;
  }

  public MealItem(
      String name,
      String category,
      List<String> timeOfDayList,
      List<String> daysOfWeekList,
      List<String> allergensList) {
    this.name = name;
    this.category = category;
    this.timeOfDayList = timeOfDayList;
    this.daysOfWeekList = daysOfWeekList;
    this.allergensList = allergensList;
  }

  public MealItem(
      String name, String category, List<String> timeOfDayList, List<String> allergensList) {
    this.name = name;
    this.category = category;
    this.timeOfDayList = timeOfDayList;
    this.allergensList = allergensList;
  }

  public String getMealItemID() {
    return mealItemID;
  }

  public void setMealItemID(String mealItemID) {
    this.mealItemID = mealItemID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public int getCalories() {
    return calories;
  }

  public void setCalories(int calories) {
    this.calories = calories;
  }

  public List<String> getTimeOfDayList() {
    return timeOfDayList;
  }

  public void setTimeOfDayList(List<String> timeOfDayList) {
    this.timeOfDayList = timeOfDayList;
  }

  public List<String> getDaysOfWeekList() {
    return daysOfWeekList;
  }

  public void setDaysOfWeekList(List<String> daysOfWeekList) {
    this.daysOfWeekList = daysOfWeekList;
  }

  public List<String> getAllergensList() {
    return allergensList;
  }

  public void setAllergensList(List<String> allergensList) {
    this.allergensList = allergensList;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof MealItem) {
      MealItem objectMealItem = (MealItem) o;
      return (this.getMealItemID().equals(objectMealItem.getMealItemID()));
    } else {
      return false;
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
