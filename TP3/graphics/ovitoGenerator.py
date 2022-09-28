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
        cell[:, 0] = (0.24, 0, 0)
        cell[:, 1] = (0, 0.09, 0)
        cell[:, 2] = (0, 0, 2)
        data.objects.append(cell)

        particles = get_particles(data_frame[frame])
        data.objects.append(particles)

    pipeline.modifiers.append(simulation_cell)

    export_file(pipeline, 'results_ovito_100_1.dump', 'lammps/dump',
                columns=["Particle Identifier", "Position.X", "Position.Y", "Force.X", "Force.Y", "Radius"],
                multiple_frames=True, start_frame=0, end_frame=len(data_frame) - 1)


def _generate_square(L: int):
    square_points = []
    for y in np.arange(0, 0.04, 0.0005):
        square_points.append([0.12, y, 0])
        square_points.append([0.12, 0.09-y, 0])
    return np.array(square_points)



def get_particle_data(frame_file):

    frames = []
    with open(frame_file, "r") as frame:
        next(frame)
        frame_lines = []
        for line in frame:
            ll = line.split()
            line_info = []
            for index in ll:
                line_info.append(float(index.replace(',', '')))
            if len(line_info) > 1:
                frame_lines.append(line_info)
            elif len(line_info) == 1:
                df = pd.DataFrame(np.array(frame_lines), columns=["id", "x", "y", "vx", "vy", "radius"])
                frames.append(df)
                frame_lines = []
        df = pd.DataFrame(np.array(frame_lines), columns=["id", "x", "y", "vx", "vy", "radius"])
        frames.append(df)
    print(frames)

    return frames


def get_particles(data_frame):
    particles = Particles()
    square_points = _generate_square(6)
    particles.create_property('Particle Identifier', data=np.concatenate((data_frame.id, np.arange(len(data_frame.x), len(data_frame.x) + len(square_points)))))
    particles.create_property("Position", data=np.concatenate((np.array((data_frame.x, data_frame.y, np.zeros(len(data_frame.x)))).T, square_points)))
    particles.create_property("Force", data=np.concatenate((np.array((data_frame.vx, data_frame.vy, np.zeros(len(data_frame.x)))).T,np.zeros((len(square_points), 3)))))
    particles.create_property('Radius', data=np.concatenate((data_frame.radius, np.full(len(square_points), 0.0005))))

    return particles


if __name__ == '__main__':
    export_to_ovito(sys.argv[1])
