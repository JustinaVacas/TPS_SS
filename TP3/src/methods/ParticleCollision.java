package methods;

import java.util.Objects;

public class ParticleCollision implements Comparable<ParticleCollision>{

    private Particle particle1;
    private Particle particle2;
    private Double tc;
    public enum CollisionWall{
        VERTICAL,
        HORIZONTAL,
        CORNER,
        TABIQUE,
        LEFT_WALL,
        RIGHT_WALL,
        UPPER_WALL,
        DOWN_WALL,
        MIDDLE_WALL,
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

    private boolean checkTypeAndParticles(final ParticleCollision collision) {
        return ((Objects.equals(particle1, collision.particle1) && Objects.equals(particle2, collision.particle2))
                ||
                (Objects.equals(particle1, collision.particle2) && Objects.equals(particle2, collision.particle1))
        )
                && wall == collision.wall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticleCollision collision = (ParticleCollision) o;
        return checkTypeAndParticles(collision);
    }

    @Override
    public int compareTo(ParticleCollision o) {
        int result = Double.compare(tc, o.tc);
//        if (result == 0) {
//            return checkTypeAndParticles(o) ? 0 : 1;
//        }
        return result;
    }

    @Override
    public String toString() {
        return "ParticleCollision{" +
                " tc=" + tc +
                ", particle1=" + particle1 +
                ", particle2=" + particle2 +
                ", tc=" + tc +
                ", wall=" + wall +
                '}';
    }

}
