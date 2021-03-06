USE [homepage]
GO
/****** Object:  Table [dbo].[Favorites2]    Script Date: 4/20/2020 6:21:21 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Favorites2](
	[InternalId] [int] IDENTITY(1,1) NOT NULL,
	[Directory] [varchar](255) NULL,
	[Name] [varchar](255) NULL,
	[SuccessFlag] [int] NULL,
	[TemplateId] [int] NULL,
	[TimeOfEntry] [datetime] NULL,
	[TimeOfLastUpdate] [datetime] NULL,
	[URL] [varchar](255) NULL,
	[UpdateString] [varchar](255) NULL,
	[scrappedCreate] [varchar](255) NULL,
	[scrappedUpdate] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[InternalId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
CREATE NONCLUSTERED INDEX [Favorites_TimeOfLastUpdate] ON [dbo].[Favorites2]
(
	[TimeOfLastUpdate] DESC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON)
GO



GO
SET ANSI_PADDING OFF
GO
