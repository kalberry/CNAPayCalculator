package com.example.kyle.cnapaycalculator;

public class CalculatePay {
    private double morningHours, eveningHours, overnightHours, regularHours, overtimeHours,
            totalHours, regularPay, overtimePay, grossPay, FICA, stateTax, federalTax, netPay,
            morningOTHours, eveningOTHours, overnightOTHours,
            morningRate, eveningRate, overnightRate;

    public CalculatePay(double nMorningHours, double nEveningHours, double nOvernightHours,
                             double nMorningOTHours, double nEveningOTHours, double nOvernightOTHours) {
        GetRates();

        morningHours = nMorningHours;
        eveningHours = nEveningHours;
        overnightHours = nOvernightHours;
        morningOTHours = nMorningOTHours;
        eveningOTHours = nEveningOTHours;
        overnightOTHours = nOvernightOTHours;


        //Calculate hours
        regularHours = morningHours + eveningHours + overnightHours;
        overtimeHours = morningOTHours + eveningOTHours + overnightOTHours;
        totalHours = regularHours + overtimeHours;

        //Calculate gross pay
        regularPay = (morningHours * morningRate) + (eveningHours * eveningRate) +
                (overnightHours * overnightRate);
        overtimePay = (morningOTHours * morningRate * 1.5) + (eveningOTHours * eveningRate * 1.5) +
                (overnightOTHours * overnightRate * 1.5);
        grossPay = regularPay + overtimePay;

        //calculate taxes
        FICA = grossPay * 0.0765;
        double withhold = 952.5;
        double plusXofExcess = .12;
        double federalTaxGross = grossPay-2*159.6;
        double periodsPerYear = 26;
        double excessOver = 13225;
        federalTax = (withhold+(plusXofExcess*(federalTaxGross*periodsPerYear-excessOver)))/periodsPerYear;
        stateTax = federalTaxGross * .0882;

        if (federalTax < 0)
            federalTax = 0;
        if (stateTax < 0)
            stateTax = 0;

        //Calculate net pay
        netPay = (grossPay - FICA - stateTax - federalTax);

    }

    public void GetRates()
    {
        morningRate = HoursCalculatorActivity.morningRate;
        eveningRate = HoursCalculatorActivity.eveningRate;
        overnightRate = HoursCalculatorActivity.overnightRate;
    }

    public double GetRegularHours()
    {
        return regularHours;
    }

    public double GetOvertimeHours()
    {
        return overtimeHours;
    }

    public double GetTotalHours()
    {
        return totalHours;
    }

    public double GetRegularPay()
    {
        return regularPay;
    }

    public double GetOvertimePay()
    {
        return overtimePay;
    }

    public double GetGrossPay()
    {
        return grossPay;
    }

    public double GetFICA()
    {
        return FICA;
    }

    public double GetStateTax()
    {
        return stateTax;
    }

    public double GetFederalTax()
    {
        return federalTax;
    }

    public double GetNetPay()
    {
        return netPay;
    }
}
    /*Calculate hours
    regularHours = morningHours + eveningHours + overnightHours;
    overtimeHours = morningOTHours + eveningOTHours + overnightOTHours;
    totalHours = regularHours + overtimeHours;

    //Calculate gross pay
    regularPay = (morningHours * morningRate) + (eveningHours * eveningRate) +
            (overnightHours * overnightRate);
    overtimePay = (morningOTHours * morningRate * 1.5) + (eveningOTHours * eveningRate * 1.5) +
            (overnightOTHours * overnightRate * 1.5);
    grossPay = regularPay + overtimePay;

    //calculate taxes
    FICA = grossPay * 0.0765;
    double withhold = 952.5;
    double plusXofExcess = .12;
    double federalTaxGross = grossPay-2*159.6;
    double periodsPerYear = 26;
    double excessOver = 13225;
    federalTax = (withhold+(plusXofExcess*(federalTaxGross*periodsPerYear-excessOver)))/periodsPerYear;
    stateTax = federalTaxGross * .0882;

        if (federalTax < 0)
    federalTax = 0;
        if (stateTax < 0)
    stateTax = 0;

    //Calculate net pay
    netPay = (grossPay - FICA - stateTax - federalTax);

        totalHoursOutput.setText(String.format("%.2f", regularHours));
        totalHoursOutputOTTV.setText(String.format("%.2f", overtimeHours));

        grossPayOutput.setText(String.format("%s%.2f", "$", regularPay));
        grossPayOTTV.setText(String.format("%s%.2f", "$", overtimePay));
        totalGrossPayTV.setText(String.format("%s%.2f", "$", grossPay));
        if (totalGrossPayTV != null)
            totalNetPayTV.setText(String.format("%s%.2f", "$", netPay));*/
