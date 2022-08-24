import sys

if __name__ == '__main__':
    text_file = open(sys.argv[1], "r")
    lines = text_file.readlines()
    myfile = open("frames.xyz", "w")
    frameNum = 0
    for line in lines:
        split = line.split()
        if len(split) > 1:
            particleId, x, y, z, vx, vy = split
            myfile.write("{}\t {}\t {}\t {}\t {}\t {}\t".format(particleId, x, y, z, vx, vy))
            myfile.write("\n")
        elif len(split) == 1:
            myfile.write(line)
    myfile.close()
    text_file.close()
