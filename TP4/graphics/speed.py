import math
import sys
import matplotlib.pyplot as plt
import numpy as np


def draw(speeds, dt):
    plt.rcParams.update({'font.size': 13})
    plt.plot(speeds, color='fuchsia')
    xticks = np.arange(0, len(speeds), step=int(len(speeds) / 20))
    plt.xticks(xticks, [i * dt for i in xticks], rotation=-90)
    plt.xlabel("Tiempo (s)")
    plt.ylabel("Modulo de la Velocidad (km/s)")
    plt.tight_layout()
    plt.show()


def parseParameters(file):
    with open(file) as statesFile:
        statesLines = statesFile.readlines()

    speeds = []
    total_days = 0
    day_steps = 1
    dt = 300
    steps_per_day = math.ceil(24 * 60 * 60 / dt)
    vxV = 0
    vyV = 0
    vxN = 0
    vyN = 0
    for line in statesLines:
        newline = line.strip()
        split = newline.split()
        if len(split) == 1:
            if float(split[0]) == 5788800.0:#8800142.5: este es el 2   #5788800.0): esto es del 1 el tiempo en que llega
                break
            else:
                continue
        elif len(split) == 5:
            if float(split[0]) == 3.0:
                speeds.append(math.sqrt(float(split[3]) ** 2 + float(split[4]) ** 2))
                vxN = float(split[3])
                vyN = float(split[4])
            if float(split[0]) == 2.0: #1 ES TIERRA
                vxV = float(split[3])
                vyV = float(split[4])
        if day_steps % steps_per_day == 0:
            total_days += 1
            day_steps = 0

        day_steps += 1

    time_elapsed = dt * day_steps

    hours = int(time_elapsed / 3600)
    minutes = int((time_elapsed - hours * 3600) / 60)
    print(total_days, hours, minutes)
    print("len speeds ", len(speeds))
    vrelativa = math.sqrt((vxV - vxN) ** 2 + (vyV - vyN) ** 2)
    print("vrelativa", vrelativa)
    return speeds


if __name__ == '__main__':
    speed = parseParameters(sys.argv[1])
    draw(speed, 300)
