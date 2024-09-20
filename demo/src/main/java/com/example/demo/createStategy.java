package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

import java.util.concurrent.ThreadLocalRandom;

import org.apache.tomcat.util.net.jsse.PEMFile;

public class createStategy {
    private int laps, tL; //no of laps, pit time loss
    private int noS, noM, noH; //no of tires
    private double eQLS, eQLM, eQLH; //expected quality loss
    private int mLS, mLM, mLH; //max laps
    private double eTLS, eTLM, eTLH; //exp. time loss per q% per lap
    private double lTS, lTM, lTH; // exp. fresh lap time

    
    public createStategy(int laps, int tL, int noS, int noM, int noH, double eQLS, double eQLM, double eQLH, int mLS,
            int mLM, int mLH, double eTLS, double eTLM, double eTLH, double lTS, double lTM, double lTH) {
        this.laps = laps;
        this.tL = tL;
        this.noS = noS;
        this.noM = noM;
        this.noH = noH;
        this.eQLS = eQLS;
        this.eQLM = eQLM;
        this.eQLH = eQLH;
        this.mLS = mLS;
        this.mLM = mLM;
        this.mLH = mLH;
        this.eTLS = eTLS;
        this.eTLM = eTLM;
        this.eTLH = eTLH;
        this.lTS = lTS;
        this.lTM = lTM;
        this.lTH = lTH;
    }

    public ArrayList<String> calculateStrategy(int numberOfPopulations){
        ArrayList<String> result = new ArrayList<>();
        ArrayList<ArrayList<String>> results = new ArrayList<>();
        ArrayList<Double> times = new ArrayList<>();
        double bestTime = Double.MAX_VALUE;
        ArrayList<String> legalTires = new ArrayList<>();
        int timeout = 0;
        if(noS > 0){
            legalTires.add("S");
        }
        if(noM > 0){
            legalTires.add("M");
        }
        if(noH > 0){
            legalTires.add("H");
        }
        for(int i = 0; i < 100; i++){
            if(timeout > 99999){
                return result; //failsafe
            }
            results.add(validateSolSwitches(generateRandom(legalTires, laps)));
            if(!validateSolMaxLaps(results.get(i))){
                results.remove(i);
                i--;
                timeout++;
            }
            else{
                times.add(calculateTime(results.get(i)));
                if(times.get(i) < bestTime){
                    bestTime = times.get(i);
                    result = results.get(i);
                }
            }
        }
        //numberofPopulations loop
        for(int j = 0; j < numberOfPopulations; j++){
            //System.err.println(bestTime);
            ArrayList<ArrayList<String>> topResults = new ArrayList<>();
            for(int i = 0; i < 10; i++){
                Double maxValue = Collections.min(times);
                int actualItem = times.indexOf(maxValue);
                topResults.add(results.get(actualItem));
                times.remove(actualItem);
                results.remove(actualItem);
            }
            results.clear();
            times.clear();
            for(int m = 0; m < topResults.size(); m++){
                for(int n = 0; n < topResults.size(); n++){
                    ArrayList<String> temp = new ArrayList<>();
                    int randomNum = ThreadLocalRandom.current().nextInt(0, laps);
                    for(int i = 0; i < randomNum; i++){
                        temp.add(topResults.get(m).get(i));
                    }
                    for(int i = randomNum; i < laps; i++){
                        temp.add(topResults.get(n).get(i));
                    }
                    results.add(temp);
                }
            }
            ArrayList<ArrayList<String>> results2 = new ArrayList<>();
            for(int i = 0; i < results.size(); i++){
                if(validateSolMaxLaps(validateSolSwitches(results.get(i)))){
                    results2.add(validateSolSwitches(results.get(i)));
                }
            }
            results.clear();
            for(int i = 0; i < results2.size(); i++){
                results.add(results2.get(i));
                times.add(calculateTime(results.get(i)));
            }
            for(int i = results2.size(); i < 100; i++){
                if(timeout > 99999){
                    return result; //failsafe
                }
                results.add(validateSolSwitches(generateRandom(legalTires, laps)));
                if(!validateSolMaxLaps(results.get(i))){
                    results.remove(i);
                    i--;
                    timeout++;
                }
                else{
                    times.add(calculateTime(results.get(i)));
                    if(times.get(i) < bestTime){
                        bestTime = times.get(i);
                        result = results.get(i);
                    }
                }
            }
            for(int i = 0; i < results.size(); i++){
                if(times.get(i) < bestTime){
                    bestTime = times.get(i);
                    result = results.get(i);
                }
            }
        }
        //loop
            //fix with 1, remove with 2
            //choose 10 best
        return result;
    }

    private Boolean validateInput(){
        if(noS == 0 && noM == 0 && noH ==0){
            return false;
        }
        return true;
    }

   private ArrayList<String> validateSolSwitches(ArrayList<String> tires){
        ArrayList<String> result = new ArrayList<>();
        Map<String, Integer> aviableTires = new HashMap<>();
        aviableTires.put("S", noS);
        aviableTires.put("M", noM);
        aviableTires.put("H", noH);
        String cur = new String();
        cur = tires.get(0);
        aviableTires.replace(cur, aviableTires.get(cur) - 1);
        result.add(cur);
        for(int i = 1; i < tires.size(); i++){
            if(tires.get(i) != cur){
                if(aviableTires.get(tires.get(i)) > 0){
                    cur = tires.get(i);
                    aviableTires.replace(cur, aviableTires.get(cur) - 1);
                    result.add(cur);
                }
                else{
                    result.add(cur);
                }
            }
            else{
                result.add(cur);
            }
        }
        return result;
    }

    private Boolean validateSolMaxLaps(ArrayList<String> tires){
        
        String curTire = new String();
        int life = 1;
        curTire = tires.get(0);
        Map<String, Integer> maxLaps = new HashMap<>();
        Map<String, Integer> maxTires = new HashMap<>();
        maxLaps.put("S", mLS);
        maxLaps.put("M", mLM);
        maxLaps.put("H", mLH);
        maxTires.put("S", noS);
        maxTires.put("M", noM);
        maxTires.put("H", noH);
        maxTires.replace(curTire, maxTires.get(curTire) - 1);
        for(int i = 1; i < tires.size(); i++){
            if(tires.get(i) == curTire){
                life++;
                if(maxLaps.get(curTire) < life){
                    if(maxTires.get(curTire) == 0){
                        return false;
                    }
                    maxTires.replace(curTire, maxTires.get(curTire) - 1);
                }
            }
            else{
                curTire = tires.get(i);
                maxTires.replace(curTire, maxTires.get(curTire) - 1);
                life = 1;
            }
        }
        return true;
    }
    private double calculateTime(ArrayList<String> tires){
        double totalTime = 0;
        String curTire = new String();
        double lapsOnTire = 1;
        curTire = tires.get(0);
        Map<String, Double> freshLaps = new HashMap<>();
        freshLaps.put("S", lTS);
        freshLaps.put("M", lTM);
        freshLaps.put("H", lTH);
        Map<String, Double> percentLoss = new HashMap<>();
        percentLoss.put("S", eQLS);
        percentLoss.put("M", eQLM);
        percentLoss.put("H", eQLH);
        Map<String, Double> lossImpact = new HashMap<>();
        lossImpact.put("S",eTLS);
        lossImpact.put("M",eTLM);
        lossImpact.put("H",eTLH);
        totalTime += freshLaps.get(curTire);
        for(int i = 1; i < tires.size(); i++){
            if(tires.get(i) == curTire){
                totalTime += freshLaps.get(curTire) + (Math.pow(1+(percentLoss.get(curTire))/100, lapsOnTire)*lossImpact.get(curTire));
                lapsOnTire++;
            }
            else{
                curTire = tires.get(i);
                lapsOnTire = 1;
                totalTime += tL;
                totalTime += freshLaps.get(curTire);
            }
        }
        return totalTime;
    }

    private ArrayList<String> generateRandom(ArrayList<String> legal, int length){
        ArrayList<String> result = new ArrayList<>();
        int randomNum = ThreadLocalRandom.current().nextInt(0, legal.size());
        result.add(legal.get(randomNum));
        for(int i = 1; i <length; i++){
            int randomNum2 = ThreadLocalRandom.current().nextInt(0, 6);
            if(randomNum2 == 3){
                randomNum = ThreadLocalRandom.current().nextInt(0, legal.size());
            }
            result.add(legal.get(randomNum));
        }
        return result;
    }
}
