import sys

import numpy as np
import pandas as pd
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file
from ovito.pipeline import StaticSource, Pipeline

def export_to_ovito(frame_file):
    data_frame = get_particle_data(frame_file, step=int(12*60*60/300)) # cada 12 horas de viaje

    pipeline = Pipeline(source=StaticSource(data=DataCollection()))

    def simulation_cell(frame, data):
        cell = SimulationCell()
        cell[:, 0] = (4, 0, 0)      # va el L donde estan los 7s
        cell[:, 1] = (0, 2, 0)
        cell[:, 2] = (0, 0, 2)
        data.objects.append(cell)

        particles = get_particles(data_frame[frame])
        data.objects.append(particles)

    pipeline.modifiers.append(simulation_cell)

    export_file(pipeline, 'results_ovito23.dump', 'lammps/dump',
                columns=["Particle Identifier", "Position.X", "Position.Y", "Position.Z", "Radius", "Force.X", "Force.Y", "Force.Z"],
                multiple_frames=True, start_frame=0, end_frame=len(data_frame) - 1)


def get_particle_data(frame_file, step):


    static_df = []
    static_df.append(695700)     # sol
    static_df.append(6371.01*30)          # tierra
    static_df.append(6051.84*30)           # venus
    static_df.append(100000)        # nave

    static = pd.DataFrame(np.array(static_df), columns=["radius"])

    frames = []
    with open(frame_file, "r") as frame:
        next(frame)
        frame_lines = []
        for line in frame:
            ll = line.split()
            line_info = []
            for index in ll:
                line_info.append(float(index))
            if len(line_info) > 1:
                frame_lines.append(line_info)
            elif len(line_info) == 1:
                df = pd.DataFrame(np.array(frame_lines), columns=["id", "x", "y", "vx", "vy"])
                frames.append(pd.concat([df, static], axis=1))
                frame_lines = []
        df = pd.DataFrame(np.array(frame_lines), columns=["id", "x", "y", "vx", "vy"])
        frames.append(pd.concat([df, static], axis=1))
    return frames[0::step]


def get_particles(data_frame):
    particles = Particles()
    particles.create_property('Particle Identifier', data=data_frame.id)
    particles.create_property("Position", data=np.array((data_frame.x/15, data_frame.y/15, np.zeros(len(data_frame.x)))).T)
    particles.create_property('Radius', data=data_frame.radius)
    particles.create_property("Force", data=np.array((data_frame.vx, data_frame.vy, np.zeros(len(data_frame.x)))).T)

    return particles


if __name__ == '__main__':
    export_to_ovito(sys.argv[1])
