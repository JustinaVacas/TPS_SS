import sys
import plotly.graph_objects as go
import matplotlib.pyplot as plt
import datetime

def drawMin(distance, t):

    plt.rcParams.update({'font.size': 40})

    data = []

    data.append(go.Scatter(
        x=t, y=distance,
        mode='lines',
        marker={'color':'fuchsia'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Fecha de salida'),
            yaxis=dict(title='Distancia (km)'),
            font=dict(size=25),
            plot_bgcolor='rgb(255,255,255)'
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.update_yaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.show()

def parseParameters(file):
    with open(file) as framesFile:
        framesLines = framesFile.readlines()

    distance = []
    dates = []
    for line in framesLines:
        newline = line.strip()
        splits = newline.split()
        fechas = splits[1].split("-")
        date = datetime.date(int(fechas[0]), int(fechas[1]), int(fechas[2]))
        distance.append(float(splits[0]))
        dates.append(date)

    return distance, dates


if __name__ == '__main__':
    distance, dates = parseParameters(sys.argv[1])
    drawMin(distance, dates)
