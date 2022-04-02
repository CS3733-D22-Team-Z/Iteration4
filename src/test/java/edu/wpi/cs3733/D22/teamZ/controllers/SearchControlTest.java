package edu.wpi.cs3733.D22.teamZ.controllers;

import static org.junit.Assert.*;

import edu.wpi.cs3733.D22.teamZ.entity.Location;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SearchControlTest {

  SearchControl control, control2;
  Location L1, L2, L3;

  @Before
  public void setUp() throws Exception {
    L1 = new Location("n", 1, 1, "1", "Tower", "REST", "Restroom 1", "R1");
    L2 = new Location("b", 1, 1, "L1", "Tower", "REST", "Restroom 2", "R2");
    L3 = new Location("r", 1, 1, "2", "Tower", "RETL", "Store", "Store");
    control = new SearchControl(List.of(L1, L2));
    control2 = new SearchControl(List.of(L1, L2, L3));
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public void addObjs() {
    control.addObjs(List.of(L3));

    assertArrayEquals(control.getCurrentSearchable().toArray(), List.of(L1, L2, L3).toArray());
  }

  @Test
  public void resetSearchableObjs() {
    control.resetSearchableObjs();
    assertArrayEquals(control.getCurrentSearchable().toArray(), List.of().toArray());
  }

  @Test
  public void filterListNormal() {
    assertArrayEquals(control.filterList("Restroom 1").toArray(), List.of(L1).toArray());
  }

  @Test
  public void filterListSingleType() {
    assertArrayEquals(control.filterList("floor::1").toArray(), List.of(L1).toArray());
  }

  @Test
  public void filterListMultitype() {
    assertArrayEquals(
        control2.filterList("\"floor::2\" \"type::retl\"").toArray(), List.of(L3).toArray());
  }

  @Test
  public void filterListEmptyType() {
    assertArrayEquals(
        control.filterList("type::").toArray(), control.getCurrentSearchable().toArray());
  }

  @Test
  public void filterListTypeAndSearch() {
    assertArrayEquals(
        control2.filterList("\"floor::2\" \"store\"").toArray(), List.of(L3).toArray());
  }

  @Test
  public void getCurrentSearchable() {
    assertArrayEquals(control.getCurrentSearchable().toArray(), List.of(L1, L2).toArray());
  }
}
