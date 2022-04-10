package edu.wpi.cs3733.D22.teamZ.entity;

import edu.wpi.cs3733.D22.teamZ.database.FacadeDAO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathEdge {

  public double getBaseCost() {
    return baseCost;
  }

  private double baseCost;
  private double cost;
  private EdgeType type;
  private Location dest;
  private Location from;
  private List<PathEdge> pathTo;

  public PathEdge(Location from, Location to) {
    if (from.getNodeType().equalsIgnoreCase("elev") && to.getNodeType().equalsIgnoreCase("elev")) {
      type = EdgeType.ELEVATOR;
    } else if (from.getNodeType().equalsIgnoreCase("stai")
        && to.getNodeType().equalsIgnoreCase("stai")) {
      type = EdgeType.STAIRS;
    } else if (from.getNodeType().equalsIgnoreCase("exit")
        || (from.getNodeType().equalsIgnoreCase("hall")
            && !to.getNodeType().equalsIgnoreCase("hall"))) {
      type = EdgeType.ENTRANCE;
    } else if (to.getNodeType().equalsIgnoreCase("exit")
        || (to.getNodeType().equalsIgnoreCase("hall")
            && !from.getNodeType().equalsIgnoreCase("hall"))) {
      type = EdgeType.EXIT;
    } else if (to.getNodeType().equalsIgnoreCase("hall")
        && from.getNodeType().equalsIgnoreCase("hall")) {
      type = EdgeType.HALL;
    } else {
      type = EdgeType.OTHER;
    }

    baseCost =
        Math.sqrt(
            Math.pow((from.getXcoord() - to.getXcoord()), 2)
                + Math.pow((from.getYcoord() - to.getYcoord()), 2));
    if (type == EdgeType.ELEVATOR) {
      baseCost = 40;
    } else if (type == EdgeType.STAIRS) {
      baseCost = 60;
    }
    cost = baseCost;
    dest = to;
    this.from = from;
    pathTo = new ArrayList<>();
  }

  public double getCost() {
    return cost;
  }

  public double getHeuristicCost(Location dest){
    double ret = 0;
    int floorFrom = 0;
    try {
      floorFrom = Integer.parseInt(from.getFloor());//normal 1-3
    }
    catch (NumberFormatException e){//actual string
      if (from.getFloor().equalsIgnoreCase("L2")){//L1 is 0, already set
        floorFrom = -1;
      }
    }

    int floorDest = 0;
    try {
      floorDest = Integer.parseInt(dest.getFloor());//normal 1-3
    }
    catch (NumberFormatException e){//actual string
      if (dest.getFloor().equalsIgnoreCase("L2")){//L1 is 0, already set
        floorDest = -1;
      }
    }
    int floorDiff = Math.abs(floorFrom - floorDest);

    Function<Double, Double> costIncrease = inAng -> .2837 * Math.pow(inAng, 2) + .707*inAng +0.1;
    //approximate exponential scaling function. .1 at 0 angle, .5 at 45, 2 at 90, 5 at 180.
    //is this good? no idea. returns a cost multiplier in a similar range.
    Comparator<Location> distSort = (o1, o2) -> {
      double dist1 = Math.sqrt(Math.pow((from.getXcoord() - o2.getXcoord()), 2) + Math.pow((from.getYcoord() - o2.getYcoord()), 2));
      double dist2 = Math.sqrt(
              Math.pow((from.getXcoord() - o1.getXcoord()), 2)
                      + Math.pow((from.getYcoord() - o1.getYcoord()), 2));
      return (int) (dist1-dist2);
    };

    FacadeDAO dao = new FacadeDAO();

    if (floorDiff == 0){//toward goal
      ret = costIncrease.apply(angleTo(dest)) * baseCost;
    }
    else if (floorDiff == 1){//nearest stair/elev
      //get by floor
      List<Location> byFloor = dao.getAllLocationsByFloor(from.getFloor());
      //get by elev
      List<Location> byElev = dao.getALlLocationsByType("ELEV");
      //get by stai
      List<Location> byStair = dao.getALlLocationsByType("STAI");

      Set<Location> elev = byElev.stream().distinct().filter(byFloor::contains).collect(Collectors.toSet());
      Set<Location> stai = byStair.stream().distinct().filter(byFloor::contains).collect(Collectors.toSet());
      Set<Location> viable = new HashSet<>();
      viable.addAll(elev);
      viable.addAll(stai);


      List<Location>nearest = viable.stream().sorted(distSort).collect(Collectors.toList());

      ret = costIncrease.apply(angleTo(nearest.get(0))) * baseCost;
    }
    else {//elev
      //get by floor
      List<Location> floor = dao.getAllLocationsByFloor(from.getFloor());
      //get by elev
      List<Location> elev = dao.getALlLocationsByType("ELEV");
      Set<Location> viable = floor.stream().distinct().filter(elev::contains).collect(Collectors.toSet());

      List<Location> nearest = viable.stream().sorted(distSort).collect(Collectors.toList());
      ret = costIncrease.apply(angleTo(nearest.get(0))) * baseCost;
    }

    return ret;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public EdgeType getType() {
    return type;
  }

  public void setType(EdgeType type) {
    this.type = type;
  }

  public Location getDest() {
    return dest;
  }

  public List<PathEdge> getPathTo() {
    return pathTo;
  }

  private enum EdgeType {
    ELEVATOR,
    STAIRS,
    ENTRANCE,
    EXIT,
    HALL,
    OTHER
  }

  public static Stream<PathEdge> findPath(Location from, Location to) {
    List<PathEdge> farthest = from.getConnections();
    Set<PathEdge> usedPathEdge = new HashSet<>();
    Set<Location> usedLocation = new HashSet<>();

    Comparator<PathEdge> sorter = (e1, e2) -> (int) (e1.getHeuristicCost(to) - e2.getHeuristicCost(to));
    farthest.sort(sorter);

    while (farthest.size() > 0) {
      PathEdge active = farthest.get(0);

      if (active.getDest().getNodeID().equalsIgnoreCase(to.getNodeID())) {
        break;
      }

      Set<PathEdge> uniques = new HashSet<>(farthest);

      for (PathEdge conn : active.getDest().getConnections()) {
        conn.getPathTo().addAll(active.getPathTo());
        conn.getPathTo().add(active);

        if (!usedLocation.contains(conn.getDest())) {
          uniques.add(conn);
          conn.setCost(active.getCost() + conn.getCost());
          usedLocation.add(conn.getDest());
        }
        usedPathEdge.add(conn);
      }

      farthest = new ArrayList<>(uniques);
      farthest.remove(active);
      farthest.sort(sorter);

      // Collections.reverse(farthest);
      usedPathEdge.add(active);
    }

    List<PathEdge> preRet = new ArrayList<>(farthest.get(0).getPathTo());

    resetUsed(usedPathEdge);

    return preRet.stream();
  }

  private static void resetUsed(Set<PathEdge> toReset) {
    for (PathEdge edge : toReset) {
      edge.getPathTo().clear();
      edge.setCost(edge.getBaseCost());
    }
  }

  private double angleTo(Location test){
    double rad1 = Math.atan2(dest.getYcoord()- from.getYcoord(), dest.getXcoord() - from.getXcoord());
    double rad2 = Math.atan2(test.getYcoord()- from.getYcoord(), test.getXcoord() - from.getXcoord());

    return Math.abs(rad1 - rad2);
  }

}
