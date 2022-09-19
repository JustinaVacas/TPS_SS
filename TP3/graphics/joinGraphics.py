import sys
import plotly.graph_objects as go
import matplotlib.pyplot as plt


def draw(data1,  data2, data3):

    plt.rcParams.update({'font.size': 30})

    data = []

    data.append(go.Scatter(
        x=data1[0], y=data1[1],
        mode='lines',
        name='50',
        marker={'color':'orange'}
    ))
    data.append(go.Scatter(
        x=data2[0], y=data2[1],
        mode='lines',
        name='100',
        marker={'color':'green'}
    ))
    data.append(go.Scatter(
        x=data3[0], y=data3[1],
        mode='lines',
        name='150',
        marker={'color':'purple'}
    ))

    fig = go.Figure(
        data=data,
        layout=go.Layout(
            xaxis=dict(title='Tiempo'),
            yaxis=dict(title='FP'),
            legend=dict(title='N - (D=0.01 m)'),
            font=dict(size=18),
            plot_bgcolor='rgb(255,255,255)'
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.update_xaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.update_yaxes(showgrid=False, linewidth=1, linecolor='black', mirror=True)
    fig.show()


def parseParameters(file,num):
    with open(file) as framesFile:
        framesLines = framesFile.readlines()

    fp = []
    time = []
    left_counter = num
    fraction = 1
    for line in framesLines:
        newline = line.strip()
        split = newline.split()
        if len(split) == 2:
            continue
        elif len(split) == 3:
            fraction = left_counter/num
            fp.append(fraction)
            left_counter = 0
            time.append(float(split[0]))
        elif len(split) == 4:
            if float(split[0]) < 0.12:
                left_counter += 1
    print("fp", fp)
    return fp, time


if __name__ == '__main__':
    fp50, time50 = parseParameters(sys.argv[1], 50)
    fp100, time100 = parseParameters(sys.argv[2], 100)
    fp150, time150 = parseParameters(sys.argv[3], 150)
    draw([time50, fp50], [time100, fp100], [time150, fp150])
