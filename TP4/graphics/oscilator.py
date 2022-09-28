import sys
import matplotlib.pyplot as plt
import numpy as np


A = 1.0
M = 70.0
K = 10000
GAMMA = 100.0


def calculate(t):
    return A * (np.exp(-(GAMMA/(2*M)) * t)) * (np.cos(np.power((K/M) - (GAMMA*GAMMA/(4*(M*M))), 0.5) * t))


def drawAll(verlet, gear):
    positionsVerlet = np.array(verlet[1])
    timesVerlet = np.array(verlet[2])
#    positionsBeeman = np.array(beeman[1])
#    timesBeeman = np.array(beeman[2])
    positionsGear = np.array(gear[1])
    timesGear = np.array(gear[2])
    real_positions = np.array(verlet[0])

    plt.plot(timesVerlet, positionsVerlet)
    plt.plot(timesGear, positionsGear)
    plt.plot(timesVerlet, real_positions)
    plt.xlabel("Tiempo (s)")
    plt.ylabel("Posicion (m)")
    plt.legend(["Verlet", "Gear", "Analitico"])
    plt.show()


def drawAllZoom(verlet, gear):

    positionsVerlet = np.array(verlet[1])
    timesVerlet = np.array(verlet[2])
    #    positionsBeeman = np.array(beeman[1])
    #    timesBeeman = np.array(beeman[2])
    positionsGear = np.array(gear[1])
    timesGear = np.array(gear[2])
    real_positions = np.array(verlet[0])

    plt.plot(timesVerlet, positionsVerlet)
    plt.plot(timesGear, positionsGear)
    plt.plot(timesVerlet, real_positions, ".")
    plt.xlabel("Tiempo (s)")
    plt.ylabel("Posicion (m)")
    plt.legend(["Verlet", "Gear", "Analitico"])
    delta = 1e-2
    to = 3.1545
    plt.xlim(to, to+delta)
    y = (calculate(to), calculate(to+delta))
    plt.ylim(min(y), max(y))
    plt.show()


def draw(data):
    positions = np.array(data[1])
    times = np.array(data[2])
    real_positions = np.array(data[0])

    plt.plot(times, positions)
    plt.plot(times, real_positions)
    plt.xlabel("Tiempo (s)")
    plt.ylabel("Posicion (m)")
    plt.legend(["Verlet", "Analitico"])
    plt.show()


def parseParameters(file):
    with open(file) as statesFile:
        statesLines = statesFile.readlines()

    r = []
    time = []
    position = []
    for line in statesLines:
        newline = line.strip()
        split = newline.split()
        if len(split) == 3:
            aux = calculate(float(split[0]))
            r.append(aux)
            position.append(float(split[1]))
            time.append(float(split[0]))
    return r, position, time


if __name__ == '__main__':
    r, position, time = parseParameters(sys.argv[1])
    r2, position2, time2 = parseParameters(sys.argv[2])
    draw([r, position, time])
    drawAll([r, position, time], [r2, position2, time2])
    drawAllZoom([r, position, time], [r2, position2, time2])

