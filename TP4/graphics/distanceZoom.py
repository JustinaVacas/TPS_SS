import sys
import plotly.graph_objects as go
import matplotlib.pyplot as plt
import datetime

def drawMin(distance, t):

    plt.rcParams.update({'font.size': 30})

    data = []

    data.append(go.Scatter(
        x=t, y=distance,
        mode='lines',
        marker={'color':'fuchsia'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(

            xaxis=dict(title='Fecha inicial'),
            yaxis=dict(title='Distancia'),
            font=dict(size=18),
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
            if count == 2:
                positionsV.append([float(split[1]), float(split[2])])
                count += 1
            elif count == 3:
                positionsN.append([float(split[1]), float(split[2])])
                count = 0
            else:
                count += 1
        if date == datetime.date(2023, 5, 12):
            break
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
    return distance, t, dates, min, dates[index]




if __name__ == '__main__':
    distance11_5_22, t11_9_22, dates11_5_22, min11_9_22, fecha_min11_5_22 = parseParameters(sys.argv[1], datetime.date(2023, 5, 11))
    drawMin(distance11_5_22, dates11_5_22)
    print("t ", dates11_5_22)

