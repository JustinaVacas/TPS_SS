import sys
import matplotlib.pyplot as plt
import numpy as np

A = 1.0
M = 70.0
K = 10000
GAMMA = 100.0


def calculate(t):
    return A * (np.exp(-(GAMMA / (2 * M)) * t)) * (np.cos(np.power((K / M) - (GAMMA * GAMMA / (4 * (M * M))), 0.5) * t))


def draw(deltas, errorsV, errorsG):

    plt.plot(deltas, errorsV, "o-", label="Verlet")
    #    plt.plot(deltas, errorsG, "o-", label="Beeman")
    plt.plot(deltas, errorsG, "o-", label="Gear")


    plt.yscale("log")
    plt.xscale("log")
    plt.legend()
    plt.ylabel("Error cuadrático medio (m^2)")
    plt.xlabel("Deltas (s)")
   # plt.rcParams.update({'font.size': 18})
    plt.show()


def parseParameters(file):
    with open(file) as statesFile:
        statesLines = statesFile.readlines()

    r = []
    time = []
    position = []
    error = 0
    for line in statesLines:
        newline = line.strip()
        split = newline.split()
        if len(split) == 3:
            aux = calculate(float(split[0]))
            r.append(aux)
            position.append(float(split[1]))
            time.append(float(split[0]))

    for x, y in zip(r, position):
        error = error + (y - x) ** 2
    return error


if __name__ == '__main__':
    deltas = [0.1, 0.05, 0.01, 0.005, 0.001, 0.0005, 0.0001, 0.00005, 0.00001]
    errorV1 = parseParameters(sys.argv[1])
    errorV2 = parseParameters(sys.argv[2])
    errorV3 = parseParameters(sys.argv[3])
    errorV4 = parseParameters(sys.argv[4])
    errorV5 = parseParameters(sys.argv[5])
    errorV6 = parseParameters(sys.argv[6])
    errorV7 = parseParameters(sys.argv[7])
    errorV8 = parseParameters(sys.argv[8])
    errorV9 = parseParameters(sys.argv[9])

    errorG1 = parseParameters(sys.argv[10])
    errorG2 = parseParameters(sys.argv[11])
    errorG3 = parseParameters(sys.argv[12])
    errorG4 = parseParameters(sys.argv[13])
    errorG5 = parseParameters(sys.argv[14])
    errorG6 = parseParameters(sys.argv[15])
    errorG7 = parseParameters(sys.argv[16])
    errorG8 = parseParameters(sys.argv[17])
    errorG9 = parseParameters(sys.argv[18])

    # errorB1 = parseParameters(sys.argv[19])
    # errorB2 = parseParameters(sys.argv[20])
    # errorB3 = parseParameters(sys.argv[21])
    # errorB4 = parseParameters(sys.argv[22])
    # errorB5 = parseParameters(sys.argv[23])
    # errorB6 = parseParameters(sys.argv[24])
    # errorB7 = parseParameters(sys.argv[25])
    # errorB8 = parseParameters(sys.argv[26])
    # errorB9 = parseParameters(sys.argv[27])

    errorsV = [errorV1, errorV2, errorV3, errorV4, errorV5, errorV6, errorV7, errorV8, errorV9]
    errorsG = [errorG1, errorG2, errorG3, errorG4, errorG5, errorG6, errorG7, errorG8, errorG9]
#    # errorsB = [errorB1, errorB2, errorB3, errorB4, errorB5, errorB6, errorB7, errorB8, errorB9]
#   draw(deltas, errorsV, errorsG, errorsB)

    draw(deltas, errorsV, errorsG)
