import sys
import plotly.graph_objects as go
import matplotlib.pyplot as plt

import datetime


def drawMin(distance, t):

    plt.rcParams.update({'font.size': 30})

    data = []

    data.append(go.Scatter(
        x=t, y=distance,
        mode='lines+markers',
        marker={'color':'fuchsia', 'size':15}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Velocidad (km/s)'),
            yaxis=dict(title='Distancia (km)'),
            font=dict(size=26),
            plot_bgcolor='rgb(255,255,255)'
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.update_yaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.show()

def parseParameters(file, initial_date):
    with open(file) as framesFile:
        framesLines = framesFile.readlines()

    positionsV = []
    positionsN = []
    t = []
    dates = []
    count = 0
    for line in framesLines:
        newline = line.strip()
        split = newline.split()
        if len(split) == 1:
            t.append(float(split[0]))
            date = initial_date + datetime.timedelta(seconds=float(split[0]))
            dates.append(date)
        elif len(split) == 5:
            if count == 1: #1 es tierra
                positionsV.append([float(split[1]), float(split[2])])
                count += 1
            elif count == 3:
                positionsN.append([float(split[1]), float(split[2])])
                count = 0
            else:
                count += 1

    distance = []
    min = 0
    xv = 0
    yv = 0
    xn = 0
    yn = 0
    aux = 0.0
    count = 0
    index = 0
    for n, v in zip(positionsN, positionsV):
        aux = ((n[0] - v[0])**2 + (n[1] - v[1])**2)**0.5 - 6051.84          # menos el radio de venus
        distance.append(aux)
        if (min > aux) & (min != 0):
            min = aux
            xv = v[0]
            yv = v[1]
            xn = n[0]
            yn = n[1]
            index = count
        if min == 0:
            min = aux
            index = count
        count += 1

    # print("distancia minima ", min)
    # print("fecha ", dates[index])
    # print("xv", xv)
    # print("yv", yv)
    # print("xn", xn)
    # print("yn", yn)
    # print("fechas", dates)
    return distance, t, dates, min, dates[index]


if __name__ == '__main__':
 #   v0s = [7.9995, 7.9996, 7.9997, 7.9998, 7.9999, 8, 8.0001, 8.0002]
    #v0s = [7.7,7.8,7.9,8,8.1,8.2,8.3,8.4,8.5,8.6,8.7,8.9]
    #v0s = [2.68, 2.78, 2.88, 2.98, 3.08, 3.18, 3.28]

    v0s = [9.9, 10, 10.1, 10.2, 10.3, 10.4]

    distance77, t77, dates77, min77, fecha_min77 = parseParameters(sys.argv[1], datetime.date(2023, 5, 13))
    distance78, t78, dates78, min78, fecha_min78 = parseParameters(sys.argv[2], datetime.date(2023, 5, 13))
    distance79, t79, dates79, min79, fecha_min79 = parseParameters(sys.argv[3], datetime.date(2023, 5, 13))
    distance8, t8, dates8, min8, fecha_min8 = parseParameters(sys.argv[4], datetime.date(2023, 5, 13))
    distance81, t81, dates81, min81, fecha_min81 = parseParameters(sys.argv[5], datetime.date(2023, 5, 13))
    distance82, t82, dates82, min82, fecha_min82 = parseParameters(sys.argv[6], datetime.date(2023, 5, 13))
   # distance83, t83, dates83, min83, fecha_min83 = parseParameters(sys.argv[7], datetime.date(2023, 5, 13))
   # distance84, t84, dates84, min84, fecha_min84 = parseParameters(sys.argv[8], datetime.date(2023, 5, 13))
    #distance85, t85, dates85, min85, fecha_min85 = parseParameters(sys.argv[9], datetime.date(2023, 5, 13))
    #distance86, t86, dates86, min86, fecha_min86 = parseParameters(sys.argv[10], datetime.date(2023, 5, 13))
   # distance87, t87, dates87, min87, fecha_min87 = parseParameters(sys.argv[11], datetime.date(2023, 5, 13))
  #  distance88, t88, dates88, min88, fecha_min88 = parseParameters(sys.argv[12], datetime.date(2023, 5, 13))

    mins = [min77, min78, min79, min8, min81, min82]#,min83]#, min84, min85, min86, min87, min88]

    drawMin(mins, v0s)


