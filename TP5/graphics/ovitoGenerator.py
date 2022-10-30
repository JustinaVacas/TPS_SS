import sys

import numpy as np
import pandas as pd
from ovito.data import DataCollection, SimulationCell, Particles
from ovito.io import export_file
from ovito.pipeline import StaticSource, Pipeline

def export_to_ovito(frame_file):
    data_frame = get_particle_data(frame_file)

    pipeline = Pipeline(source=StaticSource(data=DataCollection()))

    def simulation_cell(frame, data):
        cell = SimulationCell()
        cell[:, 0] = (22, 0, 0)      # va el L donde estan los 7s
        cell[:, 1] = (0, 22, 0)
        cell[:, 2] = (0, 0, 2)
        cell[:, 3] = (-11, -11, 0)

        data.objects.append(cell)

        particles = get_particles(data_frame[frame])
        data.objects.append(particles)

    pipeline.modifiers.append(simulation_cell)

    export_file(pipeline, 'results_ovito.dump', 'lammps/dump',
                columns=["Position.X", "Position.Y", "Position.Z", "Radius", "Color.B", "Color.R", "Color.G"],
                multiple_frames=True, start_frame=0, end_frame=len(data_frame) - 1)


def get_particle_data(frame_file):

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
                df = pd.DataFrame(np.array(frame_lines), columns=["x", "y", "radius", "blue", "red", "green"])
                frames.append(df)
                frame_lines = []
        df = pd.DataFrame(np.array(frame_lines), columns=["x", "y", "radius", "blue", "red", "green"])
        frames.append(df)
    # print(frames)
    return frames


def get_particles(data_frame):
    particles = Particles()
    particles.create_property("Position", data=np.array((data_frame.x, data_frame.y,np.zeros(len(data_frame.x)))).T)
    particles.create_property('Radius', data=data_frame.radius)
    particles.create_property('Color', data=np.array((data_frame.blue, data_frame.red, data_frame.green)).T)
    return particles

def generate_wall():
    filename = "circulo.xyz"
    with open(filename, "w") as f:
        f.write("{}\ncomment\n".format(1))
        f.write("{} {} {}\n".format(0, 0, 11))

if __name__ == '__main__':
    generate_wall()
    export_to_ovito(sys.argv[1])
