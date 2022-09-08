package methods;

public class ParticleCollision {

    private Particle particle1;
    private Particle particle2;
    private Double tc;
    public enum CollisionWall{
        VERTICAL,
        HORIZONTAL,
        CORNER
    }
    private CollisionWall wall;

    public ParticleCollision(Particle particle1, Particle particle2, Double tc, CollisionWall wall) {
        this.particle1 = particle1;
        this.particle2 = particle2;
        this.tc = tc;
        this.wall = wall;
    }

    public Particle getParticle1() {
        return particle1;
    }

    public void setParticle1(Particle particle1) {
        this.particle1 = particle1;
    }

    public Particle getParticle2() {
        return particle2;
    }

    public void setParticle2(Particle particle2) {
        this.particle2 = particle2;
    }

    public Double getTc() {
        return tc;
    }

    public void setTc(Double tc) {
        this.tc = tc;
    }

    public CollisionWall getWall() {
        return wall;
    }

    public void setWall(CollisionWall wall) {
        this.wall = wall;
    }
}
