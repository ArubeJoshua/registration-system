package org.pahappa.systems.registrationapp.views;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.pahappa.systems.registrationapp.views.DependantBean;

import java.io.Serializable;

import org.primefaces.model.chart.*;

@ManagedBean
public class ChartBean implements Serializable {

    private PieChartModel pieModel;
    private DependantBean dependantBean;
    private BarChartModel weeklyUsersDependentsModel;

    @PostConstruct
    public void init() {
        dependantBean = new DependantBean();
        createPieModels();
        createWeeklyUsersDependentsModel();
    }

    public PieChartModel getPieModel() {
        return pieModel;
    }
    private void createPieModels() {
        createPieModel();
    }
    private void createPieModel() {
        pieModel = new PieChartModel();
        dependantBean.init();
        pieModel.set("Female", dependantBean.getFemaleDependants());
        pieModel.set("Male", dependantBean.getMaleDependants());
        pieModel.setTitle("Dependent Gender Statistics");
        pieModel.setLegendPosition("c");
        pieModel.setSeriesColors("16DBCC, 1814F3");
//        pieModel.setExtender("pieChartExtender");
        pieModel.setShadow(false);
        pieModel.setShowDataLabels(true);
    }


    private void createWeeklyUsersDependentsModel() {
        weeklyUsersDependentsModel = new BarChartModel();

        ChartSeries users = new ChartSeries();
        users.setLabel("Users");
        users.set("Monday", 5);
        users.set("Tuesday", 15);
        users.set("Wednesday", 25);
        users.set("Thursday", 8);
        users.set("Friday", 11);
        users.set("Saturday", 16);
        users.set("Sunday", 9);

        ChartSeries dependents = new ChartSeries();
        dependents.setLabel("Dependents");
        dependents.set("Monday", 20);
        dependents.set("Tuesday", 2);
        dependents.set("Wednesday", 5);
        dependents.set("Thursday", 17);
        dependents.set("Friday", 20);
        dependents.set("Saturday", 4);
        dependents.set("Sunday", 21);

        weeklyUsersDependentsModel.addSeries(users);
        weeklyUsersDependentsModel.addSeries(dependents);
        weeklyUsersDependentsModel.setLegendPosition("ne");
        weeklyUsersDependentsModel.setAnimate(true);
        weeklyUsersDependentsModel.setShadow(false);
        weeklyUsersDependentsModel.setSeriesColors("1814F3,16DBCC");

        Axis xAxis = weeklyUsersDependentsModel.getAxis(AxisType.X);
        xAxis.setLabel("Day of the Week");

        Axis yAxis = weeklyUsersDependentsModel.getAxis(AxisType.Y);
        yAxis.setLabel("Count");
        yAxis.setMin(0);
        yAxis.setMax(30);
    }

    public BarChartModel getWeeklyUsersDependentsModel() {
        return weeklyUsersDependentsModel;
    }

}
