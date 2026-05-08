package service;
import domain.Lane;
import domain.RaceEvent;
import domain.Rata;
import domain.User;
import repository.DBRepoRaces;
import repository.EventRepoActions;
import validatori.ValidatorEvent;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class DBServiceRaces implements EventRepoActions<RaceEvent, User> {
    protected DBRepoRaces repo;
    public DBServiceRaces(DBRepoRaces repo) {
        this.repo=repo;
    }
    public String valideaza(String[] components){
        ValidatorEvent validatorEvent = new ValidatorEvent(components);
        return validatorEvent.valideaza();
    }
    public RaceEvent getById(Long id) {
        try {
            return utilizatori().stream()
                    .filter(p -> p.getId() == id)
                    .findFirst().get();
        }catch (NoSuchElementException e){
            return null;
        }
    }
    private List<Lane> gasesteSolutie(double T, List<Rata> rate, List<Lane> culoare) {
        List<Rata> r = new ArrayList<>(rate);

        Collections.sort(r,(rata1,rata2)->{
            if(rata1.getRezistenta()< rata2.getRezistenta())return -1;
            else return 1;
        });

        List<Lane> lanes = new ArrayList<>(culoare);
        lanes.sort(Comparator.comparingLong(Lane::getDistance));
        Set<Rata> folosite = new HashSet<>();
        Rata last = null; // rata de pe culoarul anterior

        for (Lane lane : lanes) {
            boolean found = false;
            long dist = lane.getDistance();

            for (Rata rata : r) {

                if (folosite.contains(rata)) continue;
                if (last != null && rata.getRezistenta() < last.getRezistenta())
                    continue;

                double timp = (2.0 * dist) / rata.getViteza();

                if (timp <= T) {
                    folosite.add(rata);
                    lane.setRata(rata);
                    last = rata;
                    found = true;
                    break;
                }
            }
            if (!found) return null;
        }
        return lanes;
    }
    public String ruleazaCursa(RaceEvent o, Network network) {
        RaceEvent ev = getById(o.getId());
        if (ev == null) return "Cursa nu exista";

        List<Rata> rate = new ArrayList<>(
                repo.gasesteMembrii(o, network)
                        .stream()
                        .map(u -> (Rata) u)
                        .toList()
        );

        List<Lane> culoare = new ArrayList<>(repo.gasesteCuloare(o));

        if (rate.size() < culoare.size())
            return "Nu pot fi mai puține rațe decât culoare";

        long maxDist = culoare.stream().mapToLong(Lane::getDistance).max().orElse(1);
        long minSpeed = rate.stream().mapToLong(Rata::getViteza).min().orElse(1);

        double st = 0;
        double dr = (2.0 * maxDist) / minSpeed;
        double best = dr;

        List<Lane> bestSol = null;

        while (dr - st > 1e-6) {
            double mid = (st + dr) / 2.0;

            List<Lane> sol = gasesteSolutie(mid, rate, culoare);

            if (sol != null) {
                best = mid;
                bestSol = sol;
                dr = mid;
            } else {
                st = mid;
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Timp minim: ").append(best).append("\n");

        for (Lane l : bestSol) {
            double t = (2.0 * l.getDistance()) / l.getRata().getViteza();
            sb.append("Lane ")
                    .append(l.getDistance())
                    .append(" → Rata ")
                    .append(l.getRata().getId())
                    .append(" timp: ").append(t)
                    .append("\n");
        }
        return sb.toString();
    }
    /*
    private List<Lane> gasesteSolutie(int timpMax,List<Rata> rate,List<Lane> culoare){
        float timpCurent = 0;
        Set<Rata> plasate = new HashSet<Rata>();
        for(int i=0;i<culoare.size() && timpCurent<timpMax;i++) {
            Lane culoar = culoare.get(i);
            for (int j = 0; j < rate.size(); j++) {
                Rata rata = rate.get(j);
                if (!plasate.contains(rata) && (double) culoar.getDistance() / rata.getViteza()<= rata.getRezistenta()){
                    if(i>0)
                        if(culoare.get(i-1).getRata().getRezistenta()>rata.getRezistenta())continue;
                    plasate.add(rata);
                    timpCurent+= (float) culoar.getDistance() / rata.getViteza();
                    break;
                }
            }
        }
         return (timpCurent<=timpMax) ? culoare:null;
    }
    public String ruleazaCursa(RaceEvent o,Network network){
        RaceEvent ev = getById(o.getId());
        if(ev==null)return "Cursa nu exista";
        List<Rata> participanti = new ArrayList<>(repo.gasesteMembrii(o, network).stream().map(u -> (Rata) u).toList());
        List<Lane> culoare = new ArrayList<>(repo.gasesteCuloare(o));
        if(participanti.size()<culoare.size())return "Nu pot fii mai putine rate decat culoare";
        Collections.sort(participanti,(rata1,rata2)->{
            int i = (rata1.getRezistenta() < rata2.getRezistenta()) ? -1 : 1;
            return i;
        });
        culoare.sort(Comparator.comparingLong(Lane::getDistance));
        List<Lane> solutieFinala = new ArrayList<Lane>(culoare);

        int st=0;int dr= solutieFinala.getLast().getDistance();
        float timpMin = Float.MIN_VALUE;
        while(st<dr){
            int mid = (st+dr)/2;
            List<Lane> solutie = gasesteSolutie(mid,participanti,culoare);
            if(solutie!=null){
                st=mid+1;
                timpMin=Math.max(timpMin,mid);
                for(int i=0;i<solutie.size();i++)
                    solutieFinala.get(i).setRata(solutie.get(i).getRata());
            }
            else dr=mid-1;
        }

        String rezultat="";
        for(Lane l:solutieFinala)
            rezultat+="Lane:"+solutieFinala.indexOf(l)+"Rata:"+l.getRata().getId()+"Timp:"+l.getDistance()/l.getRata().getViteza()+"\n";
        return rezultat+"Timp minim gasit per cursa "+timpMin;
    }
    */
    @Override
    public boolean gaseste(RaceEvent o) {
        return repo.gaseste(o);
    }
    @Override
    public boolean adauga(RaceEvent o) {
        return repo.adauga(o);
    }
    public boolean adaugaCuloare(RaceEvent o, List<Lane> lanes){
        try{
            lanes.stream().forEach(l->repo.adaugaCuloar(o,l));
            return true;
        }catch (RuntimeException e){
            return false;
        }
    }
    @Override
    public List<RaceEvent> utilizatori() {
        return repo.utilizatori();
    }
    public boolean inscrieParticipant(RaceEvent o, User p) {
        return repo.inscrieParticipant(o,p);
    }
    public boolean retrageParticipant(RaceEvent o, User p) {
        return repo.retrageParticipant(o,p);
    }
    public boolean poateFiAbonat(RaceEvent o,User p){
        return false;/**/
    }
    public boolean aboneaza(RaceEvent o, User p) {
        return repo.aboneaza(o,p);
    }
    public boolean dezaboneaza(RaceEvent o, User p) {
        return repo.dezaboneaza(o,p);
    }
        public List<User> gasesteMembrii(RaceEvent o, Network network) {
        return repo.gasesteMembrii(o,network);
    }
}