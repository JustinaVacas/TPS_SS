from matplotlib import pyplot as plt
import sys


def draw(particles,id,id2):

    count = 0
    id = int(id)
    id2 = int(id2)

    for part in particles:
        plt.plot(part[0], part[1], marker="o", color="grey")

    for part in particles:
        if id == count:
            plt.plot(part[0], part[1], marker="o", color="red")
        if id2 == count:
            plt.plot(part[0], part[1], marker="o", color="red")
        count += 1

    plt.show()


def parseParameters(file):
    with open(file) as dynamicFile:
        dynamicLines = dynamicFile.readlines()[1:]

    particles = []
    for line in dynamicLines:
        newline = line.strip()
        split = newline.split("   ")
        particles.append([float(split[0]), float(split[1])])

    return particles


if __name__ == '__main__':
    particles = parseParameters(sys.argv[1])
    draw(particles, sys.argv[2], sys.argv[3])
