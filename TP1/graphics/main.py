from matplotlib import pyplot as plt
import sys


def draw(particles, neighbours, id):
    count = 0
    id = int(id)

    for part in particles:
        plt.plot(part[0], part[1], marker="o", color="grey")

    for part in particles:
        if id == count:
            plt.plot(part[0], part[1], marker="o", color="red")
        else:
            for n in neighbours:
                for i in range(len(n)):
                    if (count == n[i]) & (n[0] == id):
                        plt.plot(part[0], part[1], marker="o", color="blue")
        count += 1
    plt.show()


def parseParameters(file, output):
    with open(file) as dynamicFile:
        dynamicLines = dynamicFile.readlines()[1:]

    with open(output) as outputFile:
        outputLines = outputFile.readlines()

    neighbours = []
    for line in outputLines:
        newline = line.strip()
        split = newline.split("\t")
        newsplit = split[1].strip().split(" ")
        neighbour = [split[0]]
        for n in newsplit:
            neighbour.append(n)
        count = 0
        array = []
        for n in neighbour:
            array.append(int(neighbour[count]))
            count += 1
        neighbours.append(array)
    # print(neighbours)

    particles = []
    for line in dynamicLines:
        newline = line.strip()
        split = newline.split("   ")
        particles.append([float(split[0]), float(split[1])])
    # print(particles)

    return particles, neighbours


if __name__ == '__main__':
    particles, neighbours = parseParameters(sys.argv[1], sys.argv[2])
    draw(particles, neighbours, sys.argv[3])
