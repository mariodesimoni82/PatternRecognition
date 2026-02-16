/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

/**
 *
 * @author Mario
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import model.DoublePair;
import model.Point;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class RecognitionController {

    private Set<Point> pointSet = new HashSet<>();

    @PostMapping("/point")
    public void addPoint(@RequestBody Point point) {
        pointSet.add(point);
    }

    @GetMapping("/space")
    public List<Point> getSpace() {
        return new ArrayList<>(pointSet);
    }

    @DeleteMapping("/space")
    public void deleteTest() {
        pointSet.clear();
    }

    @GetMapping("/lines/{n}")
    public List<Set<Point>> getLines(@PathVariable int n) {
        List<Set<Point>> npleList = new ArrayList<>();
        List<Point> points = new ArrayList<>(pointSet);
        if (n <= 0) {
           return npleList;
        }
        if (n == 1) {
            for (Point p : pointSet) {
                Set<Point> pset = new HashSet<>();
                pset.add(p);
                npleList.add(pset);
            }
            return npleList;
        }
        HashMap<DoublePair, Set<Point>> lineMap = new HashMap<>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point p1 = points.get(i);
                Point p2 = points.get(j);
                DoublePair key;
                if (Double.compare(p1.getX(), p2.getX()) == 0) {
                    key = new DoublePair(Double.POSITIVE_INFINITY, p1.getX());
                } else {
                    double m = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
                    double q = p1.getY() - m * p1.getX();
                    key = new DoublePair(m, q);
                }
                lineMap.putIfAbsent(key, new HashSet<>());
                lineMap.get(key).add(p1);
                lineMap.get(key).add(p2);
            }
        }
        for (Set<Point> linePoints : lineMap.values()) {
            if (linePoints.size() >= n) {
                npleList.add(linePoints);
            }
        }
        return npleList;

    }

    /* n=1 deve tonare tutti i punti
    n=2 tutte l coppie uniche
    n=3 tutte le terne uniche e cosi via
     */
 /* approccio ricorsivo in cui calcolo le nple e poi le riduco*/
    @GetMapping("/linesOld/{n}")
    public List<Set<Point>> getLinesOld(@PathVariable int n) {
        List<Set<Point>> npleList = npleList(pointSet, n);
        return npleList;

    }

    /*  dati m punti  >=  n devo ritornare tutte l nuple*/
 /* calcolo le combinazioni e poi accorpo le combinazionei equivalenti*/
    public List<Set<Point>> npleList(Set<Point> pointSet, int n) {
        List<Set<Point>> npleList = new ArrayList<>();
        if (n == 0) {
            return npleList;
        } else if (n == 1) {
            for (Point p : pointSet) {
                Set<Point> pset = new HashSet<>();
                pset.add(p);
                npleList.add(pset);
            }
            return npleList;
        } else {
            List<Set<Point>> npleMinusOneList = npleList(pointSet, n - 1);
            /* per ogni npla-1  aggiungo uno alla volta i restanti per creare la npla*/
            for (Set<Point> nplaMinusOne : npleMinusOneList) {
                Set<Point> complement = new HashSet<>(pointSet);
                complement.removeAll(nplaMinusOne);
                for (Point p : complement) {
                    Set<Point> npla = new HashSet<>(nplaMinusOne);
                    npla.add(p);
                    npleList.add(npla);
                }
            }
            List<Set<Point>> pp = new ArrayList<>(new LinkedHashSet<>(npleList));
            return unionEquivalentSet(pp);

        }

    }

    public List<Set<Point>> unionEquivalentSet(List<Set<Point>> npleList) {
        if (npleList.isEmpty() || npleList.get(0).size() < 2) {
            return npleList;
        }

        HashMap<DoublePair, Set<Point>> mqSet = new HashMap<DoublePair, Set<Point>>();
        for (Set<Point> npla : npleList) {
            Iterator<Point> it = npla.iterator();
            Point p1 = it.next();
            Point p2 = it.next();
            double m;
            double q;
            DoublePair key;
            if (Double.compare(p2.getX(), p1.getX()) == 0) {
                key = new DoublePair(Double.POSITIVE_INFINITY, p1.getX());
            } else {
                m = (p2.getY() - p1.getY()) / (p2.getX() - p1.getX());
                q = p1.getY() - m * p1.getX();
                key = new DoublePair(m, q);
            }
            if (mqSet.containsKey(key)) {
                mqSet.get(key).addAll(npla);
            } else {
                mqSet.put(key, new HashSet<>(npla));
            }
        }
        return new ArrayList<>(mqSet.values());

    }

}
